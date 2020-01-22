import './App.css';

import * as React from "react";
import './fontawesome'
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
import "./Tabler.css";

import Home from "./Components/Home/Home";
import Dashboard from "./Components/Dashboard/Dashboard";
import Workshops from "./Components/Workshops/Workshops";
import Profile from "./Components/Profile/Profile";
import EditProfile from "./Components/Profile/EditProfile";

function App() {
    return (
        <Router>
            <Switch>
                <Route exact path="/" component={Home}/>
                <Route path="/dashboard" component={Dashboard}/>
                <Route path="/workshops" component={Workshops}/>
                <Route path="/profile" component={Profile}/>
                <Route path="/editprofile" component={EditProfile}/>
            </Switch>
        </Router>
    );
}

export default App;

