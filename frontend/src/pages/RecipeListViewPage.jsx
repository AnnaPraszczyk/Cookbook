import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getRecipesList } from "../api/recipeListApi";

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

    return (
        <div className="p-6 max-w-4xl mx-auto text-white space-y-6">
            <h1 className="text-3xl font-bold mb-4">
                List: <span className="text-[#c0a060]">{listName}</span>
            </h1>

            {recipes.length === 0 ? (
                <p className="text-gray-400 italic">This list has no recipes yet.</p>
            ) : (
                <ul className="space-y-4">
                    {recipes.map((r) => (
                        <li
                            key={r.id}
                            className="p-4 border border-gray-600 rounded bg-[#2b2b2b] flex justify-between items-center"
                        >
                            <div>
                                <h3 className="text-xl font-semibold">{r.name}</h3>
                                <p className="text-gray-400 text-sm">{r.category}</p>
                            </div>
                            <button
                                onClick={() => navigate(`/recipes/${r.id}`)}
                                className="text-[#c0a060] hover:underline"
                            >
                                View Details
                            </button>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}