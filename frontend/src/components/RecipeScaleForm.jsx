// src/components/RecipeScaleForm.jsx
import React, { useState } from "react";

export default function RecipeScaleForm({ recipeId }) {
    const [portions, setPortions] = useState(1);

    const handleScale = () => {
        alert(`Recipe ${recipeId} scaled for ${portions} portion(s)!`);
    };

    return (
        <div className="bg-[#222] p-4 border border-gray-600 rounded mt-4">
            <label className="block text-gray-300 mb-2">Number of portions:</label>
            <input
                type="number"
                min="1"
                value={portions}
                onChange={(e) => setPortions(e.target.value)}
                className="p-2 w-24 bg-[#333] text-white border border-gray-500 rounded"
            />
            <button
                onClick={handleScale}
                className="ml-4 px-4 py-2 bg-[#c0a060] text-white rounded hover:bg-[#b8944d]"
            >
                Scale
            </button>
        </div>
    );
}