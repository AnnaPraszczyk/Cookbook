import React, {useEffect, useState} from "react";
import IngredientInput from "./IngredientInput";
import { HiX } from "react-icons/hi";
import { useNavigate } from "react-router-dom";
import NavigationButtons from "./NavigationButtons";

const categoryOptions = [
    "Appetizer","Soup","Main Course","Sauce","Salad","Pasta","Snack","Beverage","Dessert","Cake","Pie","Bakery"
];

const RecipeCreateForm = () => {
    const [recipeId, setRecipeId] = useState(null);
    const [recipeName, setRecipeName] = useState("");
    const [category, setCategory] = useState(categoryOptions[0]);
    const [ingredients, setIngredients] = useState([]);
    const [instructions, setInstructions] = useState("");
    const [numberOfServings, setNumberOfServings] = useState("");
    const [tags, setTags] = useState("");
    const [message, setMessage] = useState({text: '', type: ''});
    const navigate = useNavigate();
    const [productOptions, setProductOptions] = useState([]);
    const handleAddIngredient = ing =>
        setIngredients(prev => [...prev, ing]);
    const [autoCalculate, setAutoCalculate] = useState(false);
    const [formSubmitted, setFormSubmitted] = useState(false);
    const [resetCount, setResetCount] = useState(0);

    const resetForm = () => {
        setRecipeName('');
        setCategory(categoryOptions[0]);
        setIngredients([]);
        setInstructions('');
        setNumberOfServings('');
        setTags('');
        setFormSubmitted(false);
        setAutoCalculate(false);
        setMessage({ text: '', type: '' });
        setResetCount(prev => prev + 1);
    };

        const handleRemoveIngredientAt = (indexToRemove) => {
            setIngredients((prev) =>
                prev.filter((_, index) => index !== indexToRemove)
            );
        };
        useEffect(() => {
            fetch("http://localhost:8080/products")
                .then(res => res.json())
                .then(data => {
                    console.log("✅ Products from backend:", data);
                    const validProducts = data.filter(p => p && p.productName);
                    setProductOptions(validProducts);
                })
                .catch(err => console.error("❌ Error loading products", err));
        }, []);

        const handleRecipeSubmit = async (e) => {
            e.preventDefault();

            const ingredientsArray = ingredients.map(i => ({
                productName: i.product.productName.name,
                amount: i.amount,
                unit: i.unit.toUpperCase()
            }));


            const requestData = {
                recipeName,
                category,
                ingredients: ingredientsArray,
                instructions,
                numberOfServings: autoCalculate ? 0 : parseInt(numberOfServings, 10),
                tags: tags.split(",").map(s => s.trim()).filter(Boolean),
            };

            try {
                const response = await fetch('http://localhost:8080/api/recipes', {
                    method: "POST",
                    headers: {"Content-Type": "application/json"},
                    body: JSON.stringify(requestData),
                });
                if (!response.ok) {
                    const text = await response.text();
                    let errorPayload
                    try {
                        errorPayload = JSON.parse(text)
                    } catch {
                        errorPayload = text;
                    }
                    throw new Error(`Server ${response.status}: ${JSON.stringify(errorPayload)}`)
                }
                const created = await response.json();
                setMessage({text: "✅ Recipe created!", type: "success"});
                setRecipeId(created.recipeId);
                setFormSubmitted(true);
            } catch (error) {
                console.error(error);
                setMessage({text: error.message, type: "error"});
            }

        };

        return (
            <form onSubmit={handleRecipeSubmit} className="flex flex-col max-w-xl mx-auto space-y-6">
                <div>
                    <input
                        type="text"
                        placeholder="Recipe Name"
                        value={recipeName}
                        onChange={(e) => setRecipeName(e.target.value)}
                        required
                        className="p-2 text-lg border-2 border-gray-400 bg-[#333] text-whiteborder-gray-400 rounded text-gray-400 focus:outline-none focus:ring-2 w-[450px] focus:ring-white"
                    />
                </div>
                <div>
                    <select
                        value={category}
                        onChange={(e) => setCategory(e.target.value)}
                        required
                        className="p-2 flex-1 text-lg border-2 border-gray-400 rounded-md bg-[#333] text-gray-400 my-3 mr-2.5 w-[450px] h-13 focus:outline-none focus:ring-2 focus:ring-white"
                    >
                        {categoryOptions.map((option) => (
                            <option key={option} value={option}>
                                {option}
                            </option>
                        ))}
                    </select>
                </div>
                <div>
                    <IngredientInput onAdd={handleAddIngredient} productOptions={productOptions} resetCount={resetCount}/>

                    <ul className="mt-2 list-disc list-inside space-y-1">
                        {ingredients
                            .filter(i => i.product?.productName?.name)
                            .map((i, idx) => (
                                <li key={idx}
                                    className="flex items-center justify-between bg-[#333] text-white px-3 py-1 rounded">
                            <span>
                                {i.product.productName.name} - {i.amount} {i.unit}
                            </span>
                                    <button
                                        type="button"
                                        onClick={() => handleRemoveIngredientAt(idx)}
                                        className="p-0 m-0 bg-[#333] border-none ml-2 text-black-400 leading-none hover:text-red-600 text-sm font-bold"
                                    >
                                        <HiX/>
                                    </button>
                                </li>
                            ))}
                    </ul>

                </div>
                <div>
                <textarea
                    placeholder="Instructions"
                    value={instructions}
                    onChange={(e) => setInstructions(e.target.value)}
                    required
                    className="p-3 flex-1 border-2 border-gray-400 text-lg text-gray-400 bg-[#333] w-[450px] mr-2.5 my-3 rounded-md h-32 resize-y focus:outline-none focus:ring-2 focus:ring-white"
                />
                </div>
                <div>
                    <div className="flex items-center gap-2 mb-4 text-gray-300 ">
                        <input
                            type="checkbox"
                            id="autoCalculate"
                            checked={autoCalculate}
                            onChange={e => setAutoCalculate(e.target.checked)}
                            className="w-4 h-4"
                        />
                        <label htmlFor="autoCalculate" className="text-gray-300 text-sm">
                            Automatically calculate servings from ingredients
                        </label>
                    </div>
                    {!autoCalculate && (
                        <input
                            type="number"
                            min="1"
                            placeholder="Number of Servings"
                            value={numberOfServings}
                            onChange={(e) => setNumberOfServings(e.target.value)}
                            required
                            className="p-2 text-lg border-2 border-gray-400 bg-[#333] text-gray-400 rounded w-[450px] focus:outline-none focus:ring-2 focus:ring-white"
                        />
                    )}
                </div>
                <div>
                    <input
                        type="text"
                        placeholder="Tags (comma separated)"
                        value={tags}
                        onChange={(e) => setTags(e.target.value)}
                        className="p-2 text-lg border-2 border-gray-400 bg-[#333] text-whiteborder-gray-400 rounded text-gray-400 focus:outline-none focus:ring-2 w-[450px] focus:ring-white"
                    />
                </div>
                <div>
                    <button type="submit"
                            className="mt-4 text-lg px-4 py-2 bg-[#c0a060] text-white rounded hover:bg-gray-600 transition-colors duration-200">
                        Create Recipe
                    </button>
                </div>
                    {formSubmitted && message.type === "success" && (
                        <p className="mt-2 text-green-500 italic">✅ Your recipe has been created successfully!</p>
                    )}
                    {formSubmitted && recipeId && (
                        <NavigationButtons recipeId={recipeId} showNewButton={true} onAddNew={resetForm} />
                    )}
            </form>
        );
    };
export default RecipeCreateForm;