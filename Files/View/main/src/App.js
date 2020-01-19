import './App.css';
import Home from "./Components/Home/Home";
import Dashboard from "./Components/Dashboard/Dashboard";
import * as React from "react";
import './fontawesome'
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
import "tabler-react/dist/Tabler.css";

function App() {
    return (
        <Router>
            <Switch>
                <Route exact path="/" component={Home}/>
                <Route path="/dashboard" component={Dashboard}/>
            </Switch>
        </Router>
    );
}

export default App;

