import React, { useEffect, useState } from "react";
import { useParams, useNavigate, useLocation } from "react-router-dom";
import { getRecipesList, deleteRecipeFromList  } from "../api/recipeListApi";
import RecipeListView from "../components/RecipeListView";

export default function RecipeListViewPage() {
    const { listName } = useParams();
    const location = useLocation();
    const navigate = useNavigate();
    const [recipes, setRecipes] = useState([]);
    const [message, setMessage] = useState({ text: "", type: "" });
    const [showMessage, setShowMessage] = useState(false);

    const loadList = async () => {
        try {
            const res = await getRecipesList(listName);
            console.log("📦 getRecipesList response:", res);
            setRecipes(Array.isArray(res.recipes) ? res.recipes : []);
        } catch (e) {
            console.error("Failed to load recipe list", e);
        }
    };

    useEffect(() => {
        loadList().catch((err) => {
            console.error("Unhandled loadList error:", err);
        });
        if (location.state?.message) {
            setMessage(location.state.message);
            setShowMessage(true);
            setTimeout(() => setShowMessage(false), 3000);
            window.history.replaceState({}, document.title);
        }
        }, [listName]);

    function formatPortions(p) {
        return `${p} ${p === 1 ? "portion" : "portions"}`;
    }

    useEffect(() => {
        if (location.state?.refresh) {
            loadList().catch((err) => {
                console.error("Unhandled loadList error:", err);
            });
        }
    }, [location.state]);

    const handleDelete = async (entry) => {
        const confirmed = window.confirm(`Are you sure you want to remove "${entry.recipe?.recipeName}" from the list?`);
        if (!confirmed) return;
        try {
            await deleteRecipeFromList({ listName, entryId: entry.entryId });
            setRecipes(prev => prev.filter(r => r.entryId !== entry.entryId));
            setMessage({text:`"${entry.recipe?.recipeName}" has been removed from the list.`, type: "success"});
            setShowMessage(true);
            setTimeout(() => setShowMessage(false), 3000);
        } catch (e) {
            console.error("Failed to delete recipe", e);
            setMessage({text: "Something went wrong while deleting the recipe.", type: "error" });
            setShowMessage(true);
            setTimeout(() => setShowMessage(false), 3000);
        }
    };

    const handleView = (entry) => {
        navigate(`/recipes/${entry.recipe.recipeId}`, {
            state: {
                listName,
                defaultPortions: entry.portions,
                portions: entry.portions,
                fromList: true
            }
        });
    };

    /**
     * @param {{ entryId: string, portions: number, recipe?: { id: string } }} entry
     */
    const handleUpdate = (entry) => {
        const recipeId = entry.recipe?.recipeId;
        if (!recipeId) return;
        navigate(`/recipes/update/${entry.recipe.recipeId}`, {
            state: {
                listName,
                entryId: entry.entryId
            }
        });
    };

    const refreshList = async () => {
        try {
            const res = await getRecipesList(listName);
            setRecipes(Array.isArray(res.recipes) ? res.recipes : []);
        } catch (e) {
            console.error("Failed to refresh recipe list", e);
        }
    };


    return (
        <div className="p-6 max-w-4xl mx-auto text-white space-y-6">
            <h2 className="text-3xl font-bold mb-4 mt-6">
                <span>{listName}</span>
            </h2>

            {recipes.length === 0 ? (
                <p className="text-gray-400 italic">This list has no recipes yet.</p>
            ) : (
                <ul className="space-y-4">
                    {recipes.map((r) => (
                        <li
                            key={r.entryId}
                            className="p-4 border border-gray-600 rounded flex justify-between items-center">
                            <div>
                                {r.recipe && (
                                    <>
                                <h3 className="text-xl font-semibold">{r.recipe.recipeName}</h3>
                                <p className="text-gray-400 text-sm">{r.recipe.category} • {formatPortions(r.portions)}
                                </p>
                                    </>
                                    )}

                            </div>
                            <button
                                onClick={() => handleView(r)}
                                className="text-[#c0a060] hover:underline">
                                View
                            </button>
                            <button
                                onClick={() => handleUpdate(r)}
                                className="text-[#c0a060] hover:underline">
                                Update
                            </button>
                            <button
                                onClick={() => handleDelete(r)}
                                className="text-[#c0a060] hover:underline">
                                Delete
                            </button>
                        </li>
                    ))}
                </ul>
            )}
            {showMessage && (
                <div className={`fixed bottom-4 left-1/2 transform -translate-x-1/2 px-6 py-3 rounded shadow-lg transition-opacity duration-300
                ${message.type === "success" ? "mt-2 text-green-500 italic" : "text-red-600"}`}>
                    {message.text}
                </div>
            )}
            <div className="p-6 max-w-4xl mx-auto text-white space-y-6">
                <RecipeListView listName={listName} onListUpdated={refreshList} />
            </div>
        </div>
    );
}