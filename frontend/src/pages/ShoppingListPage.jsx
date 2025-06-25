import React, { useEffect, useState } from 'react';
import { getShoppingList, clearList } from '../api/recipeListApi';
import {useNavigate, useParams} from 'react-router-dom';


export default function ShoppingListPage() {
    const { listName } = useParams();
    const [items, setItems] = useState({});
    const [loading, setLoading] = useState(true);
    const [inputName, setInputName] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        const load = async () => {
            try {
                const data = await getShoppingList(listName);
                setItems(data);
            } catch (err) {
                console.error('Error while downloading shopping list:', err);
            } finally {
                setLoading(false);
            }
        };
        load();
    }, [listName]);

    const handleCreate = async () => {
        if (!inputName) return;
        try {
            await createRecipeList(inputName);
            navigate(`/shoppingList/${inputName}`);
        } catch (err) {
            console.error("Błąd przy tworzeniu listy:", err);
        }
    };

    const handleOpen = () => {
        if (inputName) {
            navigate(`/shoppingList/${inputName}`);
        }
    };

    if (loading) return <p>Loading...</p>;

    const handleClear = async () => {
        const confirmed = window.confirm("Are you sure you want to clear the list?");
        if (!confirmed) return;
        await clearList(listName);
        setItems({});
    };
    const formatItems = (items) =>
        Object.entries(items)
            .map(([item, qty]) => `${item}: ${qty}`)
            .join('\n');

    return (
        <div className="max-w-2xl mx-auto mt-10 p-4 bg-[#222] text-white rounded shadow">
            <div className="mb-6 flex flex-col sm:flex-row gap-4 justify-center items-center">
                <input
                    type="text"
                    value={inputName}
                    onChange={(e) => setInputName(e.target.value)}
                    placeholder="List name..."
                    className="px-4 py-2 rounded bg-[#333] text-white border border-gray-600 focus:outline-none"
                />
                <div className="flex gap-2">
                    <button
                        onClick={handleCreate}
                        className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded"
                    >
                        Create
                    </button>
                    <button
                        onClick={handleOpen}
                        className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded"
                    >
                        Open
                    </button>
                </div>
            </div>

            <h2 className="text-xl font-semibold mb-4">
                Shopping list for: <em className="text-yellow-400">{listName}</em>
            </h2>

            {Object.keys(items).length === 0 ? (
                <p className="text-gray-400">No products on your shopping list.</p>
            ) : (
                <ul className="list-disc list-inside space-y-2">
                    {Object.entries(items).map(([item, qty]) => (
                        <li key={item} className="flex justify-between">
                            <span>{item}</span>
                            <span className="text-gray-300">{qty}</span>
                        </li>
                    ))}
                </ul>
            )}

            {Object.keys(items).length > 0 && (
                <div className="mt-6 flex gap-4">
                    <button
                        onClick={() => navigator.clipboard.writeText(formatItems(items))}
                        className="px-4 py-2 bg-[#c0a060] text-white rounded hover:bg-[#b8944d]"
                    >
                        Copy to Clipboard
                    </button>
                    <button
                        onClick={clearList}
                        className="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700"
                    >
                        Clear List
                    </button>
                </div>
            )}
        </div>
    );
}
