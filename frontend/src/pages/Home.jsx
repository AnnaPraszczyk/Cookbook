import React from "react";
import { FaFacebook, FaInstagram, FaYoutube } from "react-icons/fa";

const Home = () => {
    return (
        <div className="flex flex-col min-h-screen pt-8">
            <div className="w-full px-4 mx-auto flex flex-wrap lg:flex-nowrap gap-6">
                 <aside className="w-full sm:w-1/3 md:w-1/5 lg:w-1/5 xl:w-1/6 2xl:w-1/6">
                    <h2 className="text-xl font-semibold mb-2">Categories</h2>
                    <ul className="text-gray-600 space-y-1">
                        <li>Appetizers</li>
                        <li>Soups</li>
                        <li>Sauces</li>
                        <li>Main courses</li>
                        <li>Pastas</li>
                        <li>Salads</li>
                        <li>Snacks</li>
                        <li>Beverages</li>
                        <li>Desserts</li>
                        <li>Cakes</li>
                        <li>Pies</li>
                        <li>Bakeries</li>
                    </ul>
                </aside>

                <main className="w-full sm:w-2/3 md:w-3/5 lg:w-3/5 xl:w-4/6 2xl:w-full text-center">
                    <img src="/src/assets/logo.png" alt="Cookbook Logo" className="w-full h-auto max-w-none mx-auto mt-5"/>
                </main>

                <aside className="w-full sm:w-1/3 md:w-1/5 lg:w-1/5 xl:w-1/6 2xl:w-1/6">
                    <h2 className="text-xl font-semibold mb-2">Recommended</h2>
                    <ul className="text-gray-600 space-y-1">
                        <li>Spaghetti Carbonara</li>
                        <li>Curry chicken</li>
                        <li>Cheesecake</li>
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