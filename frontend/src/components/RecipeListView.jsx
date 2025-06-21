import React, { useEffect, useState } from 'react';
import { getRecipesList, removeRecipe, clearList, deleteList, saveRecipeList } from '../api/recipeListApi';
import AddRecipeForm from './AddRecipeToListForm.jsx';

export default function RecipeListView({ listName }) {
    const [recipes, setRecipes] = useState([]);

    const load = async () => {
        const data = await getRecipesList(listName);
        setRecipes(data.recipes || []);
    };

    useEffect(() => {
        load();
    }, [listName]);

    const handleRemove = async (id) => {
        await removeRecipe(listName, id);
        await load();
    };

    const handleClear = async () => {
        const confirmed = window.confirm('Are you sure you want to clear the list?');
        if (confirmed) {
            await clearList(listName);
            await load();
        }
    };

    const handleDelete = async () => {
        await deleteList(listName);
        alert('List deleted!');
    };

    const handleSave = async () => {
        await saveRecipeList(listName);
        alert('List saved!');
    };

    return (
        <div>
            <h3>List: {listName}</h3>
            <AddRecipeForm listName={listName} />
            <ul>
                {recipes.map(r => (
                    <li key={r.id}>
                        {r.name} <button onClick={() => handleRemove(r.id)}>Usuń</button>
                    </li>
                ))}
            </ul>
            <button onClick={handleClear}>Clear</button>
            <button onClick={handleSave}>Save</button>
            <button onClick={handleDelete}>Delete</button>
        </div>
    );
}