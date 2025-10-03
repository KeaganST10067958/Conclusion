import 'dotenv/config';
import express from 'express';
import cors from 'cors';
import mongoose from 'mongoose';

const app = express();
app.use(cors());
app.use(express.json());

const port = process.env.PORT || 8080;

// --- Mongo ---
try {
  if (!process.env.MONGO_URI) {
    console.warn('No MONGO_URI set. /tasks endpoints will fail. /quote works.');
  } else {
    // Keep SRV string as-is; dbName comes from URI path (…/planner) or defaults
    await mongoose.connect(process.env.MONGO_URI);
    console.log('Mongo connected');
  }
} catch (e) {
  console.error('Mongo connect failed:', e.message);
}

// --- Model ---
let Task;
try {
  Task = mongoose.model(
    'Task',
    new mongoose.Schema(
      {
        userId: { type: String, required: true },
        title: { type: String, required: true },
        status: { type: String, enum: ['TODO', 'DOING', 'DONE'], default: 'TODO' },
      },
      { timestamps: true, collection: 'tasks' }
    )
  );
} catch {
  /* model already compiled (hot reload) */
}

// --- Quote of the day ---
const quotes = [
  'Small wins compound—study 20 minutes now.',
  'Show up today. Momentum beats perfection.',
  'Future you will thank present you.',
  'Consistency turns hard into habit.',
  'Start with the smallest possible step.',
  'You don’t need more time, just a start.',
  'Track the streak, not the stress.',
  'Progress, not perfection.',
  'Make it easy to begin—open the book.',
  'Done is greater than perfect.',
];

app.get('/quote/today', (_req, res) => {
  const epochDays = Math.floor(Date.now() / (24 * 60 * 60 * 1000));
  const q = quotes[Math.abs(epochDays) % quotes.length];
  res.json({ text: q });
});

// --- Tasks CRUD ---
app.get('/tasks', async (req, res) => {
  try {
    if (!Task) return res.status(503).json({ error: 'DB not configured' });
    const { userId } = req.query;
    const items = await Task.find(userId ? { userId } : {})
      .sort({ updatedAt: -1 })
      .lean();
    res.json(items);
  } catch (e) {
    console.error(e);
    res.status(500).json({ error: 'failed to fetch tasks' });
  }
});

app.post('/tasks', async (req, res) => {
  try {
    if (!Task) return res.status(503).json({ error: 'DB not configured' });
    const { userId, title, status } = req.body;
    if (!userId || !title) return res.status(400).json({ error: 'userId and title required' });
    const item = await Task.create({
      userId,
      title,
      status: status ?? 'TODO',
    });
    res.status(201).json(item);
  } catch (e) {
    console.error(e);
    res.status(500).json({ error: 'failed to create task' });
  }
});

app.patch('/tasks/:id', async (req, res) => {
  try {
    if (!Task) return res.status(503).json({ error: 'DB not configured' });
    const { id } = req.params;
    const item = await Task.findByIdAndUpdate(id, req.body, { new: true }).lean();
    if (!item) return res.status(404).end();
    res.json(item);
  } catch (e) {
    console.error(e);
    res.status(500).json({ error: 'failed to update task' });
  }
});

app.delete('/tasks/:id', async (req, res) => {
  try {
    if (!Task) return res.status(503).json({ error: 'DB not configured' });
    const { id } = req.params;
    await Task.findByIdAndDelete(id);
    res.status(204).end();
  } catch (e) {
    console.error(e);
    res.status(500).json({ error: 'failed to delete task' });
  }
});

// --- Health ---
app.get('/health', (_req, res) => res.json({ ok: true }));

app.listen(port, () => console.log(`Planner API running on :${port}`));
