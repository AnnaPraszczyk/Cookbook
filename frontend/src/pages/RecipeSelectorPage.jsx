import { useParams } from "react-router-dom";
import SearchAndAddRecipe from "../components/SearchAndAddRecipe.jsx";

export default function RecipeSelectorPage() {
    const { listName } = useParams();

    return (
        <div className="p-6 max-w-4xl mx-auto text-white space-y-6">
            <h1 className="text-3xl font-bold">Add Recipe to List: <span className="text-gray-400">{listName}</span></h1>

            <SearchAndAddRecipe listName={listName} />
        </div>
    );

}