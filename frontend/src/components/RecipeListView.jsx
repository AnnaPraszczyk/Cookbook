import React, { useEffect, useState } from 'react';
import { getRecipesList, removeRecipe, clearList, deleteList, saveRecipeList } from '../api/recipeListApi';
import AddRecipeForm from './AddRecipeForm.jsx';
import SearchAndAddRecipe from "./SearchAndAddRecipe";

export default function RecipeListView({ listName }) {
    const [recipes, setRecipes] = useState([]);
    const [defaultPortions, setDefaultPortions] = useState(1);

    useEffect(() => {
        load().then(() => {
            if (recipes.length > 0) {
                setDefaultPortions(recipes[0].portions);
            }
        });
    }, [listName]);

    const load = async () => {
        const data = await getRecipesList(listName);
        const loadedRecipes = data.recipes || [];
        setRecipes(loadedRecipes);

        if (loadedRecipes.length > 0) {
            setDefaultPortions(loadedRecipes[0].portions);
        }
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
            <SearchAndAddRecipe listName={listName} defaultPortions={defaultPortions}/>
            <div className="mt-6 flex gap-4">
                <button onClick={handleClear} className="px-4 py-2 bg-[#c0a060] text-white rounded hover:bg-gray-600 transition duration-200">Clear</button>
                <button onClick= {handleSave} className="px-4 py-2 bg-[#c0a060] text-white rounded hover:bg-gray-600 transition duration-200">Save</button>
                <button onClick={handleDelete} className="px-4 py-2 bg-[#c0a060] text-white rounded hover:bg-gray-600 transition duration-200">Delete</button>
            </div>
        </div>
    );
}