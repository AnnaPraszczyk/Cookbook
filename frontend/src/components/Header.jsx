import React from "react";

const Header = () => {
    return (
        <header className="fixed top-0 left-0 w-full h-24 px-6 flex items-center justify-between z-50">

            <div className="text-left">
                <h1 className="text-5xl font-bold leading-none">Cookbook</h1>
                <h2 className="text-xl absolute left-32 top-18 sm:left-40 md:left-44">Application</h2>
            </div>

            <div className="absolute left-1/2 transform -translate-x-1/2 font-serif text-3xl text-center whitespace-nowrap hidden sm:block">
                My favorite recipes
            </div>
        </header>
    );
};

export default Header;