import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {getShoppingList, clearList, createRecipeList, getAllLists} from "../api/recipeListApi";

export default function ShoppingListPage() {
    const { listName } = useParams();
    const navigate = useNavigate();
    const [items, setItems] = useState({});
    const [loading, setLoading] = useState(true);
    const [inputName, setInputName] = useState("");
    const [recentLists, setRecentLists] = useState([]);
    const [listsError, setListsError] = useState(null);


    useEffect(() => {
        const load = async () => {
            setLoading(true);
            try {
                const data = await getShoppingList(listName);
                setItems(data || {});
            } catch (err) {
                console.error("Failed to load shopping list:", err);
            } finally {
                setLoading(false);
            }
        };
        load();
    }, [listName]);

    useEffect(() => {
        const fetchLists = async () => {
            try {
                const data = await getAllLists();
                setRecentLists(data.slice().reverse());
            } catch (e) {
                setListsError("Failed to load recent lists.");
                console.error(e);
            }
        };
        fetchLists();
    }, []);
    const handleCreate = async () => {
        const trimmedName = inputName.trim();
        if (!trimmedName) {
            alert("List name cannot be empty!");
            return;
        }
        if (recentLists.includes(trimmedName)) {
            alert("A list with this name already exists.");
            return;
        }
        try {
            await createRecipeList(trimmedName);
            setInputName("");
            const updatedLists = await getAllLists();
            setRecentLists(updatedLists.slice().reverse());
            alert("List created successfully!");
            navigate(`/lists/${trimmedName}/select-recipes`);
        } catch (err) {
            console.error("List creation error:", err);
            alert("Failed to create list.");
        }
    };

    const handleClear = async () => {
        const confirmed = window.confirm("Are you sure you want to clear the list?");
        if (!confirmed) return;
        await clearList(listName);
        setItems({});
    };

    const formatItems = (list) =>
        Object.entries(list)
            .map(([item, qty]) => `${item}: ${qty}`)
            .join("\n");

    return (
        <div className="p-6 space-y-6 max-w-4xl mx-auto text-white">
            <h1 className="text-3xl font-bold">Shopping List</h1>

            <div className="flex gap-4 flex-col sm:flex-row items-center">
                <input
                    type="text"
                    value={inputName}
                    onChange={(e) => setInputName(e.target.value)}
                    placeholder="Enter list name"
                    className="p-2 text-lg w-[400px] border border-gray-400 bg-[#333] rounded focus:outline-none focus:ring-2 focus:ring-[#c0a060]"
                />
                <button
                    onClick={handleCreate}
                    className="mt-4 text-lg px-4 py-2 w-[100px] bg-[#c0a060] mb-4 text-white rounded hover:bg-gray-600 transition-colors duration-200">
                    Create
                </button>
                <button
                    onClick={() => navigate(`/lists/${inputName}/view`)}
                    className="mt-4 text-lg px-4 py-2 w-[100px] bg-[#c0a060] mb-4 text-white rounded hover:bg-gray-600 transition-colors duration-200"
                >
                    Open
                </button>
            </div>
            <div className="mt-4 w-full">
                <h2 className="text-xl font-semibold mb-2">Recently Created Lists</h2>
                {listsError && <p className="text-red-400">{listsError}</p>}
                {recentLists.length === 0 ? (
                    <p className="text-gray-400 italic">No lists found.</p>
                ) : (
                    <ul className="list-disc list-inside space-y-1">
                        {recentLists.map((name) => (
                            <li key={name}>
                                <button
                                    onClick={() => navigate(`/lists/${name}/view`)}
                                    className="text-[#c0a060] hover:underline"
                                >
                                    {name}
                                </button>
                            </li>
                        ))}
                    </ul>
                )}
            </div>

            <h2 className="text-xl font-semibold pt-4">
                Current list: <span className="text-[#c0a060]">{listName}</span>
            </h2>

            {loading ? (
                <p>Loading…</p>
            ) : Object.keys(items).length === 0 ? (
                <p className="text-gray-400">No items in this shopping list.</p>
            ) : (
                <ul className="list-disc list-inside space-y-2 text-lg">
                    {Object.entries(items).map(([item, qty]) => (
                        <li key={item} className="flex justify-between">
                            <span>{item}</span>
                            <span className="text-gray-300">{qty}</span>
                        </li>
                    ))}
                </ul>
            )}

            {Object.keys(items).length > 0 && (
                <div className="flex flex-wrap gap-4 pt-6">
                    <button
                        onClick={() => navigator.clipboard.writeText(formatItems(items))}
                        className="px-4 py-2 bg-[#c0a060] text-white rounded hover:bg-[#b8944d]"
                    >
                        Copy to Clipboard
                    </button>
                    <button
                        onClick={handleClear}
                        className="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700"
                    >
                        Clear List
                    </button>
                </div>
            )}
        </div>
    );
}