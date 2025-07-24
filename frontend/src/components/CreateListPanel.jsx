import React from "react";
import { Link } from "react-router-dom";

export default function CreateListPanel() {
    return (
        <div className="flex justify-between items-end mb-6 flex-wrap gap-y-4">
            <Link
                to="/lists/create"
                className="px-4 py-2 text-lg rounded transition-colors duration-200 hover:bg-[#ad9854]">
                Add List
            </Link>
        </div>
    );
}