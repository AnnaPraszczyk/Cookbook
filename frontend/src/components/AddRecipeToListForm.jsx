import React, { useState } from 'react';
import { addRecipeToList } from '../api/recipeListApi';

export default function AddRecipeForm({ listName }) {
    const [recipeId, setRecipeId] = useState('');

    const handleAdd = async () => {
        await addRecipeToList(listName, recipeId);
        alert('Recipe added to list!');
    };

    return (
        <div>
            <h4>Add recipe to list</h4>
            <input value={recipeId} onChange={e => setRecipeId(e.target.value)} placeholder="ID przepisu" />
            <button onClick={handleAdd}>Add</button>
        </div>
    );
}