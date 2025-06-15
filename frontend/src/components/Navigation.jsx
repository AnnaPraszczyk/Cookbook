import { Link } from "react-router-dom";

const Navigation = () => {
    return (
        <nav style={{ position: "fixed", left:"50%", transform: "translateX(-49%)",textAlign: "center", padding: "10px", borderBottom: "2px solid black", top: "100px", width: "100%", zIndex:"1000"}}>
            <Link to="/" style={linkStyle}>
                Home Page
            </Link>
            <Link to="/products" style={linkStyle}>
                Products
            </Link>
            <Link to="/ingredients" style={linkStyle}>
                Ingredients
            </Link>
            <Link to="/recipes" style={linkStyle}>
                Recipes
            </Link>
        </nav>
    );
};

export const linkStyle = {
    fontSize: "1.2rem",
    color: "#c0a060",
    textDecoration: "none",
    padding: "10px 20px",
    border: "2px solid transparent",
    borderRadius: "5px",
    transition: "all 0.3s ease-in-out",
};

const hoverStyle = {
    borderColor: "gold",
    backgroundColor: "#333",
    color: "white",
};

document.addEventListener("DOMContentLoaded", () => {
    const links = document.querySelectorAll("nav a");
    links.forEach(link => {
        link.addEventListener("mouseover", () => {
            Object.assign(link.style, hoverStyle);
        });
        link.addEventListener("mouseout", () => {
            Object.assign(link.style, linkStyle);
        });
    });
});


export default Navigation;