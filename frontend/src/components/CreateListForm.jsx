import React, { useState } from 'react';
import { useNavigate, Link } from "react-router-dom";
import { createRecipeList, getAllLists } from "../api/recipeListApi";



export default function CreateListForm() {
    const navigate = useNavigate();
    const [inputName, setInputName] = useState("");
    const [inputDescription, setInputDescription] = useState("");
    const [portionCount, setPortionCount] = useState(1);
    const [recentLists, setRecentLists] = useState([]);
    const [error, setError] = useState("");

    const handleCreate = async () => {
        const trimmedName = inputName.trim();
        if (!trimmedName) {
            setError("List name cannot be empty.");
            return;
        }

        try {
            const existing = await getAllLists();
            if (existing.includes(trimmedName)) {
                setError("A list with this name already exists.");
                return;
            }

            await createRecipeList({
                listName: trimmedName,
                listDescription: inputDescription,
                portions: portionCount
            });

            setError("");
            alert("List created successfully!");
            navigate(`/lists/${trimmedName}/select-recipes`);
        } catch (err) {
            console.error("List creation error:", err);
            setError("Failed to create list.");
        }
    };

    return (
        <div className="p-6 max-w-3xl mx-auto text-white space-y-6 rounded shadow">
            <h1 className="text-3xl font-bold mb-4">Create New Shopping List</h1>

            <div className="space-y-4">
                <input
                    type="text"
                    value={inputName}
                    onChange={(e) => setInputName(e.target.value)}
                    placeholder="Enter list name"
                    className="p-2 text-lg w-full border border-gray-400 bg-[#333] rounded focus:outline-none focus:ring-2 focus:ring-[#c0a060]"
                />
                <input
                    type="text"
                    value={inputDescription}
                    onChange={(e) => setInputDescription(e.target.value)}
                    placeholder="List description"
                    className="p-2 text-lg w-full h-20 border border-gray-400 bg-[#333] rounded focus:outline-none focus:ring-2 focus:ring-[#c0a060]"
                />
                <input
                    type="number"
                    min="1"
                    value={portionCount}
                    onChange={(e) => setPortionCount(parseInt(e.target.value) || 1)}
                    placeholder="Portions"
                    className="p-2 text-lg w-[150px] border border-gray-400 bg-[#333] rounded focus:outline-none focus:ring-2 focus:ring-[#c0a060]"
                />
            </div>

            {error && <p className="text-red-400">{error}</p>}

            <div className="flex flex-col items-center gap-4 pt-6">
                <button
                    onClick={handleCreate}
                    className="px-4 py-2 bg-[#c0a060] text-white rounded hover:bg-[#b8944d] transition-colors"
                >
                    Create List
                </button>

                <Link
                    to="/lists"
                    className="px-4 py-2 text-white rounded hover:bg-gray-700 transition-colors"
                >
                    ← Back to List Manager
                </Link>
            </div>
        </div>
    );
}
