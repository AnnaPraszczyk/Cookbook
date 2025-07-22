import { NavLink } from "react-router-dom";

const Navigation = () => {
    return (
        <nav className="fixed top-[100px] left-1/2 transform -translate-x-1/2 text-center py-2 border-b-2 border-black w-full z-[1000] bg-transparent">
            <ul className="flex justify-center gap-4">
                <li>
                    <NavLink
                        to="/"
                        className="text-[#c0a060] text-[1.2rem] px-5 py-2 rounded transition duration-200 hover:bg-[#333] hover:text-white"
                    >
                        Home Page
                    </NavLink>
                </li>
                <li>
                    <NavLink
                        to="/products"
                        className="text-[#c0a060] text-[1.2rem] px-5 py-2 rounded transition duration-200 hover:bg-[#333] hover:text-white"
                    >
                        Products
                    </NavLink>
                </li>
                <li>
                    <NavLink
                        to="/recipes"
                        className="text-[#c0a060] text-[1.2rem] px-5 py-2 rounded transition duration-200 hover:bg-[#333] hover:text-white"
                    >
                        Recipes
                    </NavLink>
                </li>
                <li>
                    <NavLink
                        to="/shoppingList"
                        className="text-[#c0a060] text-[1.2rem] px-5 py-2 rounded transition duration-200 hover:bg-[#333] hover:text-white"
                    >
                        Shopping List
                    </NavLink>
                </li>
            </ul>
        </nav>
    );
};

export default Navigation;