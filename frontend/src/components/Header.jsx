const Header = () => {
    return (
        <header style={{
            position: "fixed",
            top: "0",
            left: "0",
            width: "100%",
            height: "100px",
            display: "flex",
            alignItems: "center",
            justifyContent: "space-between",
            marginTop: "20px",
            marginLeft: "10px",

        }}>
            <div style={{ textAlign: "left" }}>
                <h1 style={{ fontSize: "5rem", fontWeight: "bold", marginBottom: "5px"}}>Cookbook</h1>
                <h2 style={{ fontSize: "1.8rem", marginLeft: "200px", marginTop: "-8px"}}>Application</h2>
            </div>
            <div style={{ fontFamily: "'Monotype Corsiva', cursive", fontSize: "3rem", position: "absolute", left: "50%", transform: "translateX(-50%)" }}>
                My favorite recipes
            </div>
        </header>
    );
};


export default Header;