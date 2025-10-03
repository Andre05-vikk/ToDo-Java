// ToDo Application - Frontend JavaScript

const API_BASE = 'http://localhost:8080/api/v1';

// State
let currentFilter = 'all';
let tasks = [];
let categories = [];

// Initialize app
document.addEventListener('DOMContentLoaded', () => {
    initializeApp();
});

async function initializeApp() {
    await loadCategories();
    await loadTasks();
    setupEventListeners();
    updateStatistics();
}

// Event Listeners
function setupEventListeners() {
    // Task form submission
    document.getElementById('taskForm').addEventListener('submit', handleCreateTask);

    // Category form submission
    document.getElementById('categoryForm').addEventListener('submit', handleCreateCategory);

    // Filter buttons
    document.querySelectorAll('.btn-filter').forEach(btn => {
        btn.addEventListener('click', (e) => {
            document.querySelectorAll('.btn-filter').forEach(b => b.classList.remove('active'));
            e.target.classList.add('active');
            currentFilter = e.target.dataset.filter;
            renderTasks();
        });
    });

    // Edit modal close
    document.querySelector('.close').addEventListener('click', closeEditModal);

    // Edit form submission
    document.getElementById('editTaskForm').addEventListener('submit', handleUpdateTask);

    // Close modal on outside click
    window.addEventListener('click', (e) => {
        const modal = document.getElementById('editModal');
        if (e.target === modal) {
            closeEditModal();
        }
    });
}

// API Functions

// Tasks
async function loadTasks() {
    try {
        const response = await fetch(`${API_BASE}/tasks`);
        if (!response.ok) throw new Error('Failed to load tasks');
        tasks = await response.json();
        renderTasks();
        updateStatistics();
    } catch (error) {
        console.error('Error loading tasks:', error);
        showError('Viga ülesannete laadimisel');
    }
}

async function createTask(taskData) {
    try {
        const response = await fetch(`${API_BASE}/tasks`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(taskData)
        });
        if (!response.ok) throw new Error('Failed to create task');
        return await response.json();
    } catch (error) {
        console.error('Error creating task:', error);
        throw error;
    }
}

async function updateTask(taskId, updates) {
    try {
        const response = await fetch(`${API_BASE}/tasks/${taskId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(updates)
        });
        if (!response.ok) throw new Error('Failed to update task');
        return await response.json();
    } catch (error) {
        console.error('Error updating task:', error);
        throw error;
    }
}

async function deleteTask(taskId) {
    try {
        const response = await fetch(`${API_BASE}/tasks/${taskId}`, {
            method: 'DELETE'
        });
        if (!response.ok) throw new Error('Failed to delete task');
    } catch (error) {
        console.error('Error deleting task:', error);
        throw error;
    }
}

async function completeTask(taskId) {
    try {
        const response = await fetch(`${API_BASE}/tasks/${taskId}/complete`, {
            method: 'POST'
        });
        if (!response.ok) throw new Error('Failed to complete task');
        return await response.json();
    } catch (error) {
        console.error('Error completing task:', error);
        throw error;
    }
}

async function startTask(taskId) {
    try {
        const response = await fetch(`${API_BASE}/tasks/${taskId}/start`, {
            method: 'POST'
        });
        if (!response.ok) throw new Error('Failed to start task');
        return await response.json();
    } catch (error) {
        console.error('Error starting task:', error);
        throw error;
    }
}

// Categories
async function loadCategories() {
    try {
        const response = await fetch(`${API_BASE}/categories`);
        if (!response.ok) throw new Error('Failed to load categories');
        categories = await response.json();
        renderCategories();
        updateCategoryDropdowns();
    } catch (error) {
        console.error('Error loading categories:', error);
    }
}

async function createCategory(categoryData) {
    try {
        const response = await fetch(`${API_BASE}/categories`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(categoryData)
        });
        if (!response.ok) throw new Error('Failed to create category');
        return await response.json();
    } catch (error) {
        console.error('Error creating category:', error);
        throw error;
    }
}

async function deleteCategory(categoryId) {
    try {
        const response = await fetch(`${API_BASE}/categories/${categoryId}`, {
            method: 'DELETE'
        });
        if (!response.ok) throw new Error('Failed to delete category');
    } catch (error) {
        console.error('Error deleting category:', error);
        throw error;
    }
}

// Event Handlers
async function handleCreateTask(e) {
    e.preventDefault();

    const title = document.getElementById('taskTitle').value;
    const description = document.getElementById('taskDescription').value;
    const priority = document.getElementById('taskPriority').value;
    const dueDate = document.getElementById('taskDueDate').value;
    const categoryId = document.getElementById('taskCategory').value;

    const taskData = {
        title,
        description: description || null,
        priority,
        dueDate: dueDate || null,
        categoryId: categoryId || null
    };

    try {
        await createTask(taskData);
        await loadTasks();
        e.target.reset();
        showSuccess('Ülesanne lisatud!');
    } catch (error) {
        showError('Viga ülesande loomisel');
    }
}

async function handleUpdateTask(e) {
    e.preventDefault();

    const taskId = document.getElementById('editTaskId').value;
    const updates = {
        title: document.getElementById('editTaskTitle').value,
        description: document.getElementById('editTaskDescription').value,
        status: document.getElementById('editTaskStatus').value,
        priority: document.getElementById('editTaskPriority').value
    };

    try {
        await updateTask(taskId, updates);
        await loadTasks();
        closeEditModal();
        showSuccess('Ülesanne uuendatud!');
    } catch (error) {
        showError('Viga ülesande uuendamisel');
    }
}

async function handleCompleteTask(taskId) {
    try {
        await completeTask(taskId);
        await loadTasks();
        showSuccess('Ülesanne märgitud tehtuks!');
    } catch (error) {
        showError('Viga ülesande täitmisel');
    }
}

async function handleStartTask(taskId) {
    try {
        await startTask(taskId);
        await loadTasks();
        showSuccess('Ülesanne alustatud!');
    } catch (error) {
        showError('Viga ülesande alustamisel');
    }
}

async function handleDeleteTask(taskId) {
    if (!confirm('Kas oled kindel, et soovid selle ülesande kustutada?')) {
        return;
    }

    try {
        await deleteTask(taskId);
        await loadTasks();
        showSuccess('Ülesanne kustutatud!');
    } catch (error) {
        showError('Viga ülesande kustutamisel');
    }
}

async function handleCreateCategory(e) {
    e.preventDefault();

    const name = document.getElementById('categoryName').value;
    const color = document.getElementById('categoryColor').value;

    const categoryData = { name, color };

    try {
        await createCategory(categoryData);
        await loadCategories();
        e.target.reset();
        showSuccess('Kategooria lisatud!');
    } catch (error) {
        showError('Viga kategooria loomisel');
    }
}

async function handleDeleteCategory(categoryId) {
    if (!confirm('Kas oled kindel, et soovid selle kategooria kustutada?')) {
        return;
    }

    try {
        await deleteCategory(categoryId);
        await loadCategories();
        await loadTasks();
        showSuccess('Kategooria kustutatud!');
    } catch (error) {
        showError('Viga kategooria kustutamisel');
    }
}

// Rendering
function renderTasks() {
    const tasksList = document.getElementById('tasksList');
    let filteredTasks = tasks;

    // Apply filter
    if (currentFilter === 'starred') {
        filteredTasks = tasks.filter(t => t.starred);
    } else if (currentFilter === 'overdue') {
        filteredTasks = tasks.filter(t => {
            if (!t.dueDate) return false;
            return new Date(t.dueDate) < new Date() && t.status !== 'COMPLETED';
        });
    } else if (currentFilter !== 'all') {
        filteredTasks = tasks.filter(t => t.status === currentFilter);
    }

    if (filteredTasks.length === 0) {
        tasksList.innerHTML = `
            <div class="empty-state">
                <h3>Ülesandeid ei leitud</h3>
                <p>Lisa oma esimene ülesanne ülal oleva vormiga!</p>
            </div>
        `;
        return;
    }

    tasksList.innerHTML = filteredTasks.map(task => `
        <div class="task-card ${task.starred ? 'starred' : ''} ${isOverdue(task) ? 'overdue' : ''}">
            <div class="task-header">
                <h3 class="task-title">${escapeHtml(task.title)}</h3>
                <span class="task-priority priority-${task.priority}">${getPriorityText(task.priority)}</span>
            </div>
            ${task.description ? `<p class="task-description">${escapeHtml(task.description)}</p>` : ''}
            <div class="task-meta">
                <span class="task-status status-${task.status}">${getStatusText(task.status)}</span>
                ${task.categoryName ? `<span>📁 ${escapeHtml(task.categoryName)}</span>` : ''}
                ${task.dueDate ? `<span>📅 ${formatDate(task.dueDate)}</span>` : ''}
                ${task.starred ? '<span>⭐ Tärniga</span>' : ''}
            </div>
            <div class="task-actions">
                ${task.status !== 'COMPLETED' ? `<button class="btn-complete" onclick="handleCompleteTask('${task.id}')">✓ Valmis</button>` : ''}
                ${task.status === 'PENDING' ? `<button class="btn-start" onclick="handleStartTask('${task.id}')">▶ Alusta</button>` : ''}
                <button class="btn-edit" onclick="openEditModal('${task.id}')">✎ Muuda</button>
                <button class="btn-star" onclick="toggleStar('${task.id}')">${task.starred ? '★' : '☆'}</button>
                <button class="btn-delete" onclick="handleDeleteTask('${task.id}')">🗑 Kustuta</button>
            </div>
        </div>
    `).join('');
}

function renderCategories() {
    const categoriesList = document.getElementById('categoriesList');

    if (categories.length === 0) {
        categoriesList.innerHTML = '<p style="color: #95a5a6;">Kategooriad puuduvad</p>';
        return;
    }

    categoriesList.innerHTML = categories.map(cat => `
        <div class="category-tag" style="background-color: ${cat.color}">
            <span>${escapeHtml(cat.name)}</span>
            <button class="delete-category" onclick="handleDeleteCategory('${cat.id}')">×</button>
        </div>
    `).join('');
}

function updateCategoryDropdowns() {
    const select1 = document.getElementById('taskCategory');

    select1.innerHTML = '<option value="">Ilma kategooriata</option>' +
        categories.map(cat => `<option value="${cat.id}">${escapeHtml(cat.name)}</option>`).join('');
}

function updateStatistics() {
    document.getElementById('totalTasks').textContent = tasks.length;
    document.getElementById('pendingTasks').textContent = tasks.filter(t => t.status === 'PENDING').length;
    document.getElementById('inProgressTasks').textContent = tasks.filter(t => t.status === 'IN_PROGRESS').length;
    document.getElementById('completedTasks').textContent = tasks.filter(t => t.status === 'COMPLETED').length;
}

// Modal
function openEditModal(taskId) {
    const task = tasks.find(t => t.id === taskId);
    if (!task) return;

    document.getElementById('editTaskId').value = task.id;
    document.getElementById('editTaskTitle').value = task.title;
    document.getElementById('editTaskDescription').value = task.description || '';
    document.getElementById('editTaskStatus').value = task.status;
    document.getElementById('editTaskPriority').value = task.priority;

    document.getElementById('editModal').style.display = 'block';
}

function closeEditModal() {
    document.getElementById('editModal').style.display = 'none';
}

async function toggleStar(taskId) {
    const task = tasks.find(t => t.id === taskId);
    if (!task) return;

    try {
        await updateTask(taskId, { starred: !task.starred });
        await loadTasks();
    } catch (error) {
        showError('Viga tärni muutmisel');
    }
}

// Utility Functions
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleString('et-EE', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

function isOverdue(task) {
    if (!task.dueDate || task.status === 'COMPLETED') return false;
    return new Date(task.dueDate) < new Date();
}

function getStatusText(status) {
    const texts = {
        'PENDING': 'Ootel',
        'IN_PROGRESS': 'Pooleli',
        'COMPLETED': 'Tehtud',
        'CANCELLED': 'Tühistatud'
    };
    return texts[status] || status;
}

function getPriorityText(priority) {
    const texts = {
        'LOW': 'Madal',
        'MEDIUM': 'Keskmine',
        'HIGH': 'Kõrge',
        'CRITICAL': 'Kriitiline'
    };
    return texts[priority] || priority;
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

function showSuccess(message) {
    // Simple alert for now - could be improved with toast notifications
    console.log('✓ ' + message);
}

function showError(message) {
    console.error('✗ ' + message);
    alert(message);
}
