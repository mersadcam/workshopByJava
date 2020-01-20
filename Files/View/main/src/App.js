import './App.css';

import * as React from "react";
import './fontawesome'
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
import "tabler-react/dist/Tabler.css";


import Home from "./Components/Home/Home";
import Dashboard from "./Components/Dashboard/Dashboard";
import Workshops from "./Components/Workshops/Workshops";
import Profile from "./Components/Profile/Profile";

function App() {
    return (
        <Router>
            <Switch>
                <Route exact path="/" component={Home}/>
                <Route path="/dashboard" component={Dashboard}/>
                <Route path="/workshops" component={Workshops}/>
                <Route path="/profile" component={Profile}/>
            </Switch>
        </Router>
    );
}

export default App;

