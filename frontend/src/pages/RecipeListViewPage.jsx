import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getRecipesList, deleteRecipeFromList  } from "../api/recipeListApi";
import RecipeListView from "../components/RecipeListView";

export default function RecipeListViewPage() {
    const { listName } = useParams();
    const navigate = useNavigate();
    const [recipes, setRecipes] = useState([]);

    useEffect(() => {
        const load = async () => {
            try {
                const res = await getRecipesList(listName);
                setRecipes(res.recipes || []);
            } catch (e) {
                console.error("Failed to load recipe list", e);
            }
        };
        load();
    }, [listName]);

    const handleDelete = async (entryId) => {
        const confirmed = window.confirm("Are you sure you want to remove this recipe from the list?");
        if (!confirmed) return;
        try {
            await deleteRecipeFromList({ listName, entryId });
            setRecipes(prev => prev.filter(r => r.id !== entryId));
        } catch (e) {
            console.error("Failed to delete recipe", e);
        }
    };

    return (
        <div className="p-6 max-w-4xl mx-auto text-white space-y-6">
            <h2 className="text-3xl font-bold mb-4">
                <span>{listName}</span>
            </h2>

            {recipes.length === 0 ? (
                <p className="text-gray-400 italic">This list has no recipes yet.</p>
            ) : (
                <ul className="space-y-4">
                    {recipes.map((r) => (
                        <li
                            key={r.id}
                            className="p-4 border border-gray-600 rounded flex justify-between items-center"
                        >
                            <div>
                                <h3 className="text-xl font-semibold">{r.name}</h3>
                                <p className="text-gray-400 text-sm">{r.category} • {r.portions} portion {r.portions > 1 ? "s" : ""}</p>
                            </div>
                            <button
                                onClick={() => navigate(`/recipes/${r.id}?listName=${listName}`)}
                                className="text-[#c0a060] hover:underline">
                                View
                            </button>
                            <button
                                onClick={() => navigate(`/recipes/update/${r.id}?listName=${listName}`)}
                                className="text-[#c0a060] hover:underline">
                                Update
                            </button>
                            <button
                                onClick={() => handleDelete(r.entryId)}
                                className="text-[#c0a060] hover:underline">
                                Delete
                            </button>
                        </li>
                    ))}
                </ul>
            )}
            <div className="p-6 max-w-4xl mx-auto text-white space-y-6">
                <RecipeListView listName={listName} />
            </div>
        </div>
    );
}