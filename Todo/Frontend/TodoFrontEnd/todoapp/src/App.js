import React, { useState, useEffect } from 'react';
import { Plus, Edit2, Trash2, Check, X, BarChart3, Filter, Calendar, AlertCircle } from 'lucide-react';

const TodoApp = () => {
  const [todos, setTodos] = useState([]);
  const [filteredTodos, setFilteredTodos] = useState([]);
  const [newTodo, setNewTodo] = useState({
    title: '',
    description: '',
    priority: 'LOW',
    category: 'OTHER',
    expiryDate: ''
  });
  const [editingTodo, setEditingTodo] = useState(null);
  const [showAddForm, setShowAddForm] = useState(false);
  const [filter, setFilter] = useState({ category: '', priority: '' });
  const [stats, setStats] = useState(null);
  const [showStats, setShowStats] = useState(false);
  const [loading, setLoading] = useState(false);

  const API_BASE = 'http://localhost:8080/todos';

  const priorities = ['LOW', 'MEDIUM', 'HIGH'];
  const categories = ['PERSONAL', 'STUDY', 'OTHER', 'WORK', 'HEALTH', 'HOME'];

  useEffect(() => {
    fetchTodos();
    fetchStats();
  }, []);

  useEffect(() => {
    applyFilters();
  }, [todos, filter]);

  const fetchTodos = async () => {
    setLoading(true);
    try {
      const response = await fetch(API_BASE);
      const data = await response.json();
      setTodos(data);
    } catch (error) {
      console.error('Error fetching todos:', error);
    }
    setLoading(false);
  };

  const fetchStats = async () => {
    try {
      const response = await fetch(`${API_BASE}/analytics`);
      const data = await response.json();
      setStats(data);
    } catch (error) {
      console.error('Error fetching stats:', error);
    }
  };

  const applyFilters = () => {
    let filtered = todos;
    
    if (filter.category) {
      filtered = filtered.filter(todo => todo.category === filter.category);
    }
    
    if (filter.priority) {
      filtered = filtered.filter(todo => todo.priority === filter.priority);
    }
    
    setFilteredTodos(filtered);
  };

  const addTodo = async () => {
    if (!newTodo.title.trim()) return;
    
    try {
      const todoData = {
        ...newTodo,
        expiryDate: newTodo.expiryDate ? new Date(newTodo.expiryDate).toISOString() : null
      };
      
      await fetch(`${API_BASE}/addTodo`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(todoData)
      });
      
      setNewTodo({ title: '', description: '', priority: 'LOW', category: 'OTHER', expiryDate: '' });
      setShowAddForm(false);
      fetchTodos();
      fetchStats();
    } catch (error) {
      console.error('Error adding todo:', error);
    }
  };

  const updateTodo = async (id, todo) => {
    try {
      await fetch(`${API_BASE}/update/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(todo)
      });
      
      setEditingTodo(null);
      fetchTodos();
      fetchStats();
    } catch (error) {
      console.error('Error updating todo:', error);
    }
  };

  const deleteTodo = async (id) => {
    try {
      await fetch(`${API_BASE}/${id}`, { method: 'DELETE' });
      fetchTodos();
      fetchStats();
    } catch (error) {
      console.error('Error deleting todo:', error);
    }
  };

  const toggleComplete = async (id, isCompleted) => {
    try {
      const endpoint = isCompleted ? 'markAsUncompleted' : 'markAsCompleted';
      await fetch(`${API_BASE}/${endpoint}/${id}`, { method: 'PUT' });
      fetchTodos();
      fetchStats();
    } catch (error) {
      console.error('Error toggling todo:', error);
    }
  };

  const markAllCompleted = async () => {
    try {
      await fetch(`${API_BASE}/markAllAsCompleted`, { method: 'PUT' });
      fetchTodos();
      fetchStats();
    } catch (error) {
      console.error('Error marking all completed:', error);
    }
  };

  const markAllUncompleted = async () => {
    try {
      await fetch(`${API_BASE}/markAllAsUncompleted`, { method: 'PUT' });
      fetchTodos();
      fetchStats();
    } catch (error) {
      console.error('Error marking all uncompleted:', error);
    }
  };

  const deleteAllTodos = async () => {
    if (window.confirm('Are you sure you want to delete all todos?')) {
      try {
        await fetch(`${API_BASE}/deleteAll`, { method: 'DELETE' });
        fetchTodos();
        fetchStats();
      } catch (error) {
        console.error('Error deleting all todos:', error);
      }
    }
  };

  const getPriorityColor = (priority) => {
    switch (priority) {
      case 'HIGH': return 'bg-red-100 text-red-800 border-red-200';
      case 'MEDIUM': return 'bg-yellow-100 text-yellow-800 border-yellow-200';
      case 'LOW': return 'bg-green-100 text-green-800 border-green-200';
      default: return 'bg-gray-100 text-gray-800 border-gray-200';
    }
  };

  const getCategoryColor = (category) => {
    const colors = {
      PERSONAL: 'bg-purple-100 text-purple-800',
      STUDY: 'bg-blue-100 text-blue-800',
      WORK: 'bg-indigo-100 text-indigo-800',
      HEALTH: 'bg-pink-100 text-pink-800',
      HOME: 'bg-orange-100 text-orange-800',
      OTHER: 'bg-gray-100 text-gray-800'
    };
    return colors[category] || 'bg-gray-100 text-gray-800';
  };

  const formatDate = (dateString) => {
    if (!dateString) return '';
    return new Date(dateString).toLocaleDateString();
  };

  const isExpired = (expiryDate) => {
    if (!expiryDate) return false;
    return new Date(expiryDate) < new Date();
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
      <div className="container mx-auto px-4 py-8 max-w-6xl">
        {/* Header */}
        <div className="text-center mb-8">
          <h1 className="text-4xl font-bold text-gray-800 mb-2">Todo Manager</h1>
          <p className="text-gray-600">Organize your tasks efficiently</p>
        </div>

        {/* Action Buttons */}
        <div className="flex flex-wrap gap-4 mb-6 justify-center">
          <button
            onClick={() => setShowAddForm(!showAddForm)}
            className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg flex items-center gap-2 transition-colors"
          >
            <Plus size={20} />
            Add Todo
          </button>
          <button
            onClick={() => setShowStats(!showStats)}
            className="bg-purple-600 hover:bg-purple-700 text-white px-4 py-2 rounded-lg flex items-center gap-2 transition-colors"
          >
            <BarChart3 size={20} />
            Analytics
          </button>
          <button
            onClick={markAllCompleted}
            className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-lg transition-colors"
          >
            Mark All Complete
          </button>
          <button
            onClick={markAllUncompleted}
            className="bg-yellow-600 hover:bg-yellow-700 text-white px-4 py-2 rounded-lg transition-colors"
          >
            Mark All Incomplete
          </button>
          <button
            onClick={deleteAllTodos}
            className="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded-lg transition-colors"
          >
            Delete All
          </button>
        </div>

        {/* Add Todo Form */}
        {showAddForm && (
          <div className="bg-white rounded-xl shadow-lg p-6 mb-6 border border-gray-200">
            <h3 className="text-xl font-semibold mb-4 text-gray-800">Add New Todo</h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <input
                type="text"
                placeholder="Title"
                value={newTodo.title}
                onChange={(e) => setNewTodo({...newTodo, title: e.target.value})}
                className="border border-gray-300 rounded-lg px-4 py-2 focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              />
              <input
                type="date"
                value={newTodo.expiryDate}
                onChange={(e) => setNewTodo({...newTodo, expiryDate: e.target.value})}
                className="border border-gray-300 rounded-lg px-4 py-2 focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              />
              <select
                value={newTodo.priority}
                onChange={(e) => setNewTodo({...newTodo, priority: e.target.value})}
                className="border border-gray-300 rounded-lg px-4 py-2 focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              >
                {priorities.map(p => (
                  <option key={p} value={p}>{p}</option>
                ))}
              </select>
              <select
                value={newTodo.category}
                onChange={(e) => setNewTodo({...newTodo, category: e.target.value})}
                className="border border-gray-300 rounded-lg px-4 py-2 focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              >
                {categories.map(c => (
                  <option key={c} value={c}>{c}</option>
                ))}
              </select>
            </div>
            <textarea
              placeholder="Description"
              value={newTodo.description}
              onChange={(e) => setNewTodo({...newTodo, description: e.target.value})}
              className="w-full mt-4 border border-gray-300 rounded-lg px-4 py-2 focus:ring-2 focus:ring-blue-500 focus:border-transparent h-24"
            />
            <div className="flex gap-4 mt-4">
              <button
                onClick={addTodo}
                className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg transition-colors"
              >
                Add Todo
              </button>
              <button
                onClick={() => setShowAddForm(false)}
                className="bg-gray-500 hover:bg-gray-600 text-white px-6 py-2 rounded-lg transition-colors"
              >
                Cancel
              </button>
            </div>
          </div>
        )}

        {/* Stats Panel */}
        {showStats && stats && (
          <div className="bg-white rounded-xl shadow-lg p-6 mb-6 border border-gray-200">
            <h3 className="text-xl font-semibold mb-4 text-gray-800">Task Analytics</h3>
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
              <div className="text-center p-4 bg-blue-50 rounded-lg">
                <div className="text-2xl font-bold text-blue-600">{stats.totalTasks || 0}</div>
                <div className="text-gray-600">Total Tasks</div>
              </div>
              <div className="text-center p-4 bg-green-50 rounded-lg">
                <div className="text-2xl font-bold text-green-600">{stats.completedTasks || 0}</div>
                <div className="text-gray-600">Completed</div>
              </div>
              <div className="text-center p-4 bg-yellow-50 rounded-lg">
                <div className="text-2xl font-bold text-yellow-600">{stats.pendingTasks || 0}</div>
                <div className="text-gray-600">Pending</div>
              </div>
              <div className="text-center p-4 bg-red-50 rounded-lg">
                <div className="text-2xl font-bold text-red-600">{stats.expiredTasks || 0}</div>
                <div className="text-gray-600">Expired</div>
              </div>
            </div>
          </div>
        )}

        {/* Filters */}
        <div className="bg-white rounded-xl shadow-lg p-4 mb-6 border border-gray-200">
          <div className="flex flex-wrap gap-4 items-center">
            <Filter size={20} className="text-gray-600" />
            <select
              value={filter.category}
              onChange={(e) => setFilter({...filter, category: e.target.value})}
              className="border border-gray-300 rounded-lg px-3 py-1 text-sm focus:ring-2 focus:ring-blue-500"
            >
              <option value="">All Categories</option>
              {categories.map(c => (
                <option key={c} value={c}>{c}</option>
              ))}
            </select>
            <select
              value={filter.priority}
              onChange={(e) => setFilter({...filter, priority: e.target.value})}
              className="border border-gray-300 rounded-lg px-3 py-1 text-sm focus:ring-2 focus:ring-blue-500"
            >
              <option value="">All Priorities</option>
              {priorities.map(p => (
                <option key={p} value={p}>{p}</option>
              ))}
            </select>
            <button
              onClick={() => setFilter({ category: '', priority: '' })}
              className="text-blue-600 hover:text-blue-800 text-sm underline"
            >
              Clear Filters
            </button>
          </div>
        </div>

        {/* Todos List */}
        {loading ? (
          <div className="text-center py-8">
            <div className="inline-block animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
          </div>
        ) : (
          <div className="grid gap-4">
            {filteredTodos.length === 0 ? (
              <div className="text-center py-12 bg-white rounded-xl shadow-lg">
                <p className="text-gray-500 text-lg">No todos found</p>
              </div>
            ) : (
              filteredTodos.map((todo) => (
                <div
                  key={todo.id}
                  className={`bg-white rounded-xl shadow-lg border-l-4 p-6 transition-all hover:shadow-xl ${
                    todo.isCompleted 
                      ? 'border-green-500 bg-green-50' 
                      : isExpired(todo.expiryDate)
                      ? 'border-red-500 bg-red-50'
                      : 'border-blue-500'
                  }`}
                >
                  {editingTodo === todo.id ? (
                    <EditForm 
                      todo={todo} 
                      onSave={(updatedTodo) => updateTodo(todo.id, updatedTodo)}
                      onCancel={() => setEditingTodo(null)}
                      priorities={priorities}
                      categories={categories}
                    />
                  ) : (
                    <div className="flex items-start justify-between">
                      <div className="flex-1">
                        <div className="flex items-center gap-3 mb-2">
                          <h3 className={`text-xl font-semibold ${
                            todo.isCompleted ? 'line-through text-gray-500' : 'text-gray-800'
                          }`}>
                            {todo.title}
                          </h3>
                          {isExpired(todo.expiryDate) && !todo.isCompleted && (
                            <AlertCircle size={20} className="text-red-500" />
                          )}
                        </div>
                        
                        {todo.description && (
                          <p className={`text-gray-600 mb-3 ${
                            todo.isCompleted ? 'line-through' : ''
                          }`}>
                            {todo.description}
                          </p>
                        )}
                        
                        <div className="flex flex-wrap gap-2 mb-3">
                          <span className={`px-3 py-1 rounded-full text-xs font-medium border ${getPriorityColor(todo.priority)}`}>
                            {todo.priority}
                          </span>
                          <span className={`px-3 py-1 rounded-full text-xs font-medium ${getCategoryColor(todo.category)}`}>
                            {todo.category}
                          </span>
                          {todo.expiryDate && (
                            <span className={`px-3 py-1 rounded-full text-xs font-medium flex items-center gap-1 ${
                              isExpired(todo.expiryDate) ? 'bg-red-100 text-red-800' : 'bg-blue-100 text-blue-800'
                            }`}>
                              <Calendar size={12} />
                              {formatDate(todo.expiryDate)}
                            </span>
                          )}
                        </div>
                        
                        <div className="text-xs text-gray-500">
                          Created: {formatDate(todo.createdAt)}
                          {todo.updatedAt && ` • Updated: ${formatDate(todo.updatedAt)}`}
                        </div>
                      </div>
                      
                      <div className="flex gap-2 ml-4">
                        <button
                          onClick={() => toggleComplete(todo.id, todo.isCompleted)}
                          className={`p-2 rounded-lg transition-colors ${
                            todo.isCompleted
                              ? 'bg-yellow-100 text-yellow-600 hover:bg-yellow-200'
                              : 'bg-green-100 text-green-600 hover:bg-green-200'
                          }`}
                          title={todo.isCompleted ? 'Mark as incomplete' : 'Mark as complete'}
                        >
                          {todo.isCompleted ? <X size={18} /> : <Check size={18} />}
                        </button>
                        <button
                          onClick={() => setEditingTodo(todo.id)}
                          className="p-2 bg-blue-100 text-blue-600 rounded-lg hover:bg-blue-200 transition-colors"
                          title="Edit todo"
                        >
                          <Edit2 size={18} />
                        </button>
                        <button
                          onClick={() => deleteTodo(todo.id)}
                          className="p-2 bg-red-100 text-red-600 rounded-lg hover:bg-red-200 transition-colors"
                          title="Delete todo"
                        >
                          <Trash2 size={18} />
                        </button>
                      </div>
                    </div>
                  )}
                </div>
              ))
            )}
          </div>
        )}
      </div>
    </div>
  );
};

const EditForm = ({ todo, onSave, onCancel, priorities, categories }) => {
  const [editData, setEditData] = useState({
    title: todo.title || '',
    description: todo.description || '',
    priority: todo.priority || 'LOW',
    category: todo.category || 'OTHER',
    expiryDate: todo.expiryDate ? new Date(todo.expiryDate).toISOString().split('T')[0] : ''
  });

  const handleSave = () => {
    const updatedTodo = {
      ...todo,
      ...editData,
      expiryDate: editData.expiryDate ? new Date(editData.expiryDate).toISOString() : null
    };
    onSave(updatedTodo);
  };

  return (
    <div className="space-y-4">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <input
          type="text"
          value={editData.title}
          onChange={(e) => setEditData({...editData, title: e.target.value})}
          className="border border-gray-300 rounded-lg px-4 py-2 focus:ring-2 focus:ring-blue-500 focus:border-transparent"
        />
        <input
          type="date"
          value={editData.expiryDate}
          onChange={(e) => setEditData({...editData, expiryDate: e.target.value})}
          className="border border-gray-300 rounded-lg px-4 py-2 focus:ring-2 focus:ring-blue-500 focus:border-transparent"
        />
        <select
          value={editData.priority}
          onChange={(e) => setEditData({...editData, priority: e.target.value})}
          className="border border-gray-300 rounded-lg px-4 py-2 focus:ring-2 focus:ring-blue-500 focus:border-transparent"
        >
          {priorities.map(p => (
            <option key={p} value={p}>{p}</option>
          ))}
        </select>
        <select
          value={editData.category}
          onChange={(e) => setEditData({...editData, category: e.target.value})}
          className="border border-gray-300 rounded-lg px-4 py-2 focus:ring-2 focus:ring-blue-500 focus:border-transparent"
        >
          {categories.map(c => (
            <option key={c} value={c}>{c}</option>
          ))}
        </select>
      </div>
      <textarea
        value={editData.description}
        onChange={(e) => setEditData({...editData, description: e.target.value})}
        className="w-full border border-gray-300 rounded-lg px-4 py-2 focus:ring-2 focus:ring-blue-500 focus:border-transparent h-24"
      />
      <div className="flex gap-4">
        <button
          onClick={handleSave}
          className="bg-green-600 hover:bg-green-700 text-white px-6 py-2 rounded-lg transition-colors"
        >
          Save
        </button>
        <button
          onClick={onCancel}
          className="bg-gray-500 hover:bg-gray-600 text-white px-6 py-2 rounded-lg transition-colors"
        >
          Cancel
        </button>
      </div>
    </div>
  );
};

export default TodoApp;