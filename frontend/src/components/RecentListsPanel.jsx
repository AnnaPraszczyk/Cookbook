import React from "react";
import { useNavigate } from "react-router-dom";

export default function RecentListsPanel({ recentLists }) {
    const navigate = useNavigate();

    return (
        <div className="mt-4 w-full">
            <h2 className="text-xl font-semibold mb-2">Recently Created Lists</h2>
            {recentLists.length === 0 ? (
                <p className="text-gray-400 italic sm:text-base">No lists found.</p>
            ) : (
                <ul className="list-disc list-inside space-y-1">
                    {recentLists.map((name) => (
                        <li key={name}>
                            <button
                                onClick={() => navigate(`/lists/${name}/view`)}
                                className="text-[#c0a060] hover:underline sm:text-base">
                                {name}
                            </button>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}