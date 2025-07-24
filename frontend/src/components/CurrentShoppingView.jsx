import React from "react";

export default function CurrentShoppingView({ listName, items, onClear }) {
    const formatItems = (list) =>
        Object.entries(list).map(([item, qty]) => `${item}: ${qty}`).join("\n");

    return (
        <div className="mt-6 space-y-4">
            <h2 className="text-xl font-semibold pt-4">
                Current list: <span className="text-[#c0a060]">{listName}</span>
            </h2>

            {Object.keys(items).length === 0 ? (
                <p className="text-gray-400">No items in this shopping list.</p>
            ) : (
                <>
                    <ul className="list-disc list-inside space-y-2 text-lg">
                        {Object.entries(items).map(([item, qty]) => (
                            <li key={item} className="flex justify-between">
                                <span>{item}</span>
                                <span className="text-gray-300">{qty}</span>
                            </li>
                        ))}
                    </ul>

                    <div className="flex flex-wrap gap-4 pt-4">
                        <button
                            onClick={() => navigator.clipboard.writeText(formatItems(items))}
                            className="px-4 py-2 bg-[#c0a060] text-white rounded hover:bg-[#b8944d]"
                        >
                            Copy to Clipboard
                        </button>
                        <button
                            onClick={onClear}
                            className="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700"
                        >
                            Clear List
                        </button>
                    </div>
                </>
            )}
        </div>
    );
}