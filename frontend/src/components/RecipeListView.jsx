import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getRecipesList, clearList, deleteList, getShoppingList } from '../api/recipeListApi';
import { jsPDF } from "jspdf";
import SearchAndAddRecipe from "./SearchAndAddRecipe";

export default function RecipeListView({ listName, onListUpdated }) {
    const [recipes, setRecipes] = useState([]);
    const [defaultPortions, setDefaultPortions] = useState(1);
    const navigate = useNavigate();
    const [message, setMessage] = useState({ text: "", type: "" });

    const load = async () => {
        try {
            const data = await getRecipesList(listName);
            const loadedRecipes = data.recipes || [];
            setRecipes(loadedRecipes);
            setDefaultPortions(loadedRecipes.length > 0 ? loadedRecipes[0].portions : 1);
        } catch (e) {
            console.error("Failed to load recipe list", e);
        }
    };

    useEffect(() => {
        const fetchData = async () => {
            await load();
        };
        fetchData().catch((err) => {
            console.error("Unhandled fetchData error:", err);
        });
    }, [listName]);

    const handleClear = async () => {
        const confirmed = window.confirm('Are you sure you want to clear the list?');
        if (!confirmed) return;
        try {
            await clearList(listName);
            if (onListUpdated) {
                await onListUpdated();
            }
        } catch (e) {
            console.error("Failed to clear list", e);
        }
    };

    const handleDelete = async () => {
        const confirmed = window.confirm(`Are you sure you want to permanently delete the list "${listName}"?`);
        if (!confirmed) return;
        try {
            await deleteList(listName);
            setMessage({ text: "✅ List deleted successfully!", type: "success" });
            setTimeout(() => navigate("/shoppingList"), 3000);
        } catch (e) {
            console.error("Failed to delete list", e);
            setMessage({ text: "❌ Something went wrong while deleting the list.", type: "error" });
        }
    };
    const handleGenerateShoppingList = async () => {
        try {
            const shoppingList = await getShoppingList(listName);
            if (!shoppingList || Object.keys(shoppingList).length === 0) {
                alert("Shopping list is empty. Please add some recipes to the list before generating the shopping list.");
                return;
            }

            const doc = new jsPDF();
            doc.setFontSize(16);
            doc.text("Shopping list", 20, 20);

            let y = 30;
            Object.entries(shoppingList).forEach(([product, amount]) => {
                doc.text(`${product}: ${amount.toFixed(2)} g`, 20, y);
                y += 10;
            });

            doc.save(`shopping_list_${listName}.pdf`);
        } catch (error) {
            console.error("An error occurred while generating the shopping list", error);
            alert("Failed to generate the shopping list.");
        }
    };

    return (
        <div className="max-w-4xl mx-auto">
            <SearchAndAddRecipe listName={listName} defaultPortions={defaultPortions}/>
            <div className="mt-6 flex gap-4 sm:flex-row">
                <button onClick={handleGenerateShoppingList} className="px-4 py-2 bg-[#c0a060] text-white rounded hover:bg-gray-600 transition duration-200 sm:text-base">Shopping list</button>
                <button onClick={handleClear} className="px-4 py-2 bg-[#c0a060] text-white rounded hover:bg-gray-600 transition duration-200 sm:text-base">Clear List</button>
                <button onClick={handleDelete} className="px-4 py-2 bg-[#c0a060] text-white rounded hover:bg-gray-600 transition duration-200 sm:text-base">Delete List</button>
            </div>
            {message.text && (
                <p className={`mt-4 text-sm ${message.type === "success" ? "text-green-500" : "text-red-500"}`}>
                    {message.text}
                </p>
            )}
        </div>
    );
}