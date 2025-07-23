import React, { useState } from 'react';
import { addRecipeToList } from '../api/recipeListApi';

export default function AddRecipeForm({ listName }) {
    const [recipeId, setRecipeId] = useState('');
    const [portions, setPortions] = useState(1);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState('');

    const handleAdd = async () => {
        setLoading(true);
        setError(null);
        setSuccess('');
        try {
            await addRecipeToList({listName, recipeId, portions});
            setSuccess('Recipe added successfully!');
            setRecipeId('');
            setPortions(1);
        } catch (err) {
            console.error(err);
            setError('Failed to add recipe.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="p-4 bg-[#2c2c2c] rounded-md text-white space-y-2">
            <h4 className="text-lg font-semibold">Add recipe to list</h4>
            <input
                type="number"
                min="1"
                value={portions}
                onChange={(e) => setPortions(parseInt(e.target.value))}
                placeholder="Portions"
                className="p-2 w-full border border-gray-500 bg-[#333] rounded"
            />
            <button
                onClick={handleAdd}
                disabled={loading}
                className="px-4 py-2 bg-[#c0a060] text-white rounded hover:bg-[#b8944d] disabled:opacity-50"
            >
                {loading ? 'Adding...' : 'Add Recipe'}
            </button>

            {error && <p className="text-red-400">{error}</p>}
            {success && <p className="text-green-400">{success}</p>}

        </div>
    );
}