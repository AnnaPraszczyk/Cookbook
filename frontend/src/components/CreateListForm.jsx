import React, { useState } from 'react';
import { createRecipeList } from '../api/recipeListApi';

export default function CreateListForm() {
    const [listName, setListName] = useState('');

    const handleCreate = async () => {
        await createRecipeList({ listName, listDescription: "" });
        alert('The list has been created!');
    };

    return (
        <div>
            <h3>Create new list</h3>
            <input value={listName} onChange={e => setListName(e.target.value)} placeholder="Nazwa listy" />
            <button onClick={handleCreate}>Create</button>
        </div>
    );
}