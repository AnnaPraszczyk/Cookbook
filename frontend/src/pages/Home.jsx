import React from "react";
import { useEffect, useState } from "react";
import { FaFacebook, FaInstagram, FaYoutube } from "react-icons/fa";
import { Link } from "react-router-dom";

const categories = [
    { value: "APPETIZER", label: "Appetizer" },
    { value: "SOUP", label: "Soup" },
    { value: "SAUCE", label: "Sauce" },
    { value: "MAIN_COURSE", label: "Main Course" },
    { value: "PASTA", label: "Pasta" },
    { value: "SALAD", label: "Salad" },
    { value: "SNACK", label: "Snack" },
    { value: "BEVERAGE", label: "Beverage" },
    { value: "DESSERT", label: "Dessert" },
    { value: "CAKE", label: "Cake" },
    { value: "PIE", label: "Pie" },
    { value: "BAKERY", label: "Bakery" }
];

const Home = () => {
    const [latestRecipes, setLatestRecipes] = useState([]);
    useEffect(() => {
        fetch("http://localhost:8080/api/recipes/latest")
            .then(res => {
                if (!res.ok) throw new Error("Failed to fetch latest recipes");
                return res.json();
            })
            .then(data => setLatestRecipes(data))
            .catch(err => console.error("❌ Error during loading latest recipes:", err));
    }, []);

    return (
        <div className="flex flex-col min-h-screen pt-8 mt-2">
            <div className="w-full px-4 mx-auto flex flex-wrap lg:flex-nowrap gap-6">
                 <aside className="w-full text-left text-lg sm:w-1/3 md:w-1/5 lg:w-1/5 xl:w-1/6 2xl:w-1/6">
                    <h2 className="text-xl font-semibold mb-2">Categories</h2>
                     <ul>
                     {categories.map(({ value, label }) => (
                         <li key={value}>
                             <Link
                                   to={`/recipes/search?category=${value}&page=0`}
                                   className="hover:text-[#c0a060] hover:underline">
                                 {label}
                             </Link>
                         </li>
                     ))}
                     </ul>
                 </aside>

                <main className="w-full sm:w-2/3 md:w-3/5 lg:w-3/5 xl:w-4/6 2xl:w-full text-center">
                    <img src="/src/assets/logo.png" alt="Cookbook Logo" className="w-full h-auto max-w-none mx-auto mt-5"/>
                </main>

                <aside className="w-full sm:w-1/3 md:w-1/5 lg:w-1/5 xl:w-1/6 2xl:w-1/6">
                    <h2 className="text-xl font-semibold mb-2">Latest</h2>
                    <ul className="text-gray-500 text-lg">
                        {latestRecipes.length === 0 ? (
                            <li className="italic text-gray-400">Loading...</li>
                        ) : (
                            latestRecipes.map(recipe => (
                                <li key={recipe.id}>
                                    <Link
                                        to={`/recipes/${recipe.id}`}
                                        className="hover:text-[#c0a060] hover:underline">
                                        {recipe.name.length > 32 ? recipe.name.slice(0, 32) + "..." : recipe.name}
                                    </Link>
                                </li>
                            ))
                        )}
                    </ul>
                </aside>
            </div>

            <footer className="mt-auto text-center py-6 text-sm text-gray-600">
                &copy; {new Date().getFullYear()} Cookbook App
                <div className="text-2xl mt-2 flex justify-center gap-4">

                <a href="https://www.facebook.com" target="_blank" rel="noopener noreferrer" className="text-blue-600 hover:text-blue-800">
                        <FaFacebook />
                    </a>
                    <a href="https://www.instagram.com" target="_blank" rel="noopener noreferrer" className="text-pink-500 hover:text-pink-700">
                        <FaInstagram />
                    </a>
                    <a href="https://www.youtube.com" target="_blank" rel="noopener noreferrer" className="text-red-600 hover:text-red-800">
                        <FaYoutube />
                    </a>
                </div>
            </footer>
        </div>
    );
};
export default Home;