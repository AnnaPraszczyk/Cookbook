import { useNavigate } from "react-router-dom";

const NavigationButtons = ({ recipeId, showNewButton, onAddNew }) => {
    const navigate = useNavigate();

    return (
        <div className="flex gap-10 mt-6 sm:flex-row sm:items-start">
            {showNewButton && (
                <button className="mt-4 w-32 text-lg px-4 py-2 bg-[#c0a060] text-white rounded hover:bg-gray-600 transition-colors duration-200"
                    onClick={onAddNew}>Add New</button>)}
            <button className="mt-4 w-32 text-lg px-4 py-2 bg-[#c0a060] text-white rounded hover:bg-gray-600 transition-colors duration-200"
                    onClick={() => navigate(`/recipes/${recipeId}`)}>Scale</button>
            <button className="mt-4 w-32 text-lg px-4 py-2 bg-[#c0a060] text-white rounded hover:bg-gray-600 transition-colors duration-200"
                    onClick={() => navigate(`/recipes/update/${recipeId}`)}>Update</button>
            <button className="mt-4 w-32 text-lg px-4 py-2 bg-[#c0a060] text-white rounded hover:bg-gray-600 transition-colors duration-200"
                    onClick={() => navigate(`/recipes/delete/${recipeId}`)}>Delete</button>
        </div>
    );
};

export default NavigationButtons;