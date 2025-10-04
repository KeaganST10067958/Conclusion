import 'dotenv/config';
import express from 'express';
import cors from 'cors';
import mongoose from 'mongoose';

const app = express();
app.use(cors());
app.use(express.json());

const port = process.env.PORT || 8080;

/* ---------- DB ---------- */
let Task; // defined after connect

async function connectMongo() {
  const uri = process.env.MONGO_URI;
  if (!uri) {
    console.warn('No MONGO_URI set. /tasks endpoints will return 503.');
    return;
  }
  try {
    await mongoose.connect(uri);
    console.log('Mongo connected');

    // define schema once (after connect to avoid model overwrite warnings)
    const TaskSchema = new mongoose.Schema(
      {
        userId: { type: String, required: true },
        title: { type: String, required: true },
        status: {
          type: String,
          enum: ['TODO', 'DOING', 'DONE'],
          default: 'TODO'
        }
      },
      { timestamps: true }
    );

    Task = mongoose.models.Task || mongoose.model('Task', TaskSchema);
  } catch (e) {
    console.error('Mongo connect failed:', e.message);
  }
}
await connectMongo();

/* ---------- Routes ---------- */

// health
app.get('/health', (_, res) => res.json({ ok: true }));

// quotes (deterministic by day)
const QUOTES = [
  'Small wins compound—study 20 minutes now.',
  'Show up today. Momentum beats perfection.',
  'Future you will thank present you.',
  'Consistency turns hard into habit.',
  'Start with the smallest possible step.',
  'You don’t need more time, just a start.',
  'Track the streak, not the stress.',
  'Progress, not perfection.',
  'Make it easy to begin—open the book.',
  'Done is greater than perfect.'
];

app.get('/quote/today', (_req, res) => {
  const epochDays = Math.floor(Date.now() / 86_400_000);
  const text = QUOTES[Math.abs(epochDays) % QUOTES.length];
  res.json({ text });
});

// guard if DB missing
function requireDB(req, res, next) {
  if (!Task) return res.status(503).json({ error: 'DB not configured' });
  next();
}

// list tasks (optionally by userId)
app.get('/tasks', requireDB, async (req, res) => {
  const { userId } = req.query;
  const q = userId ? { userId } : {};
  const items = await Task.find(q).sort({ createdAt: -1 });
  res.json(items);
});

// create task
app.post('/tasks', requireDB, async (req, res) => {
  const { userId, title, status } = req.body || {};
  if (!userId || !title) {
    return res.status(400).json({ error: 'userId and title are required' });
  }
  const item = await Task.create({ userId, title, status: status ?? 'TODO' });
  res.status(201).json(item);
});

// update task
app.patch('/tasks/:id', requireDB, async (req, res) => {
  const { id } = req.params;
  const item = await Task.findByIdAndUpdate(id, req.body, { new: true });
  if (!item) return res.status(404).end();
  res.json(item);
});

// delete task
app.delete('/tasks/:id', requireDB, async (req, res) => {
  const { id } = req.params;
  await Task.findByIdAndDelete(id);
  res.status(204).end();
});

// root info
app.get('/', (_req, res) =>
  res.json({
    name: 'Planner API',
    endpoints: ['/health', '/quote/today', 'GET/POST /tasks', 'PATCH/DELETE /tasks/:id']
  })
);

app.listen(port, () => console.log(`Planner API running on :${port}`));
