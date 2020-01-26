import './App.css';

import * as React from "react";
import './fontawesome'
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";

import Home from "./Components/Home/Home";
import Dashboard from "./Components/Dashboard/Dashboard";
import Workshops from "./Components/Workshops/Workshops";
import Profile from "./Components/Profile/Profile";
import EditProfile from "./Components/Profile/EditProfile";
import Login from "./Components/Home/Login";
import Workshop from "./Components/Workshop/Workshop";
import StudentWorkshop from "./Components/StudentWorkshop/StudentWorkshop";
import TeacherWorkshop from "./Components/TeacherWorkshop/TeacherWorkshop";
import CreateForm from "./Components/Forms/CreateForm";
import ViewForm from "./Components/Forms/ViewForm";
import axios from "axios";
import CreateWorkshop from "./Components/CreateWorkshop/CreateWorkshop";
import RootWorkshop from "./Components/RootWorkshop/RootWorkshop";
axios.defaults.headers.common['token'] = localStorage.getItem("token");
axios.defaults.headers.common['userType'] = localStorage.getItem("userType");

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state={

        }
    }


    render(){

        return (
            <Router>
                <Switch>
                    <Route exact path="/" component={Home}/>
                    <Route path="/dashboard" component={Dashboard}/>
                    <Route path="/workshops" component={Workshops}/>
                    <Route path="/profile/:usernameURL" component={Profile}/>
                    <Route path="/editprofile" component={EditProfile}/>
                    <Route path="/login" component={Login}/>
                    <Route path="/rootworkshop/:workshopID" component={RootWorkshop}/>
                    <Route path="/createworkshop" component={CreateWorkshop}/>
                    <Route path="/createform/:workshopID" component={CreateForm}/>
                    <Route path="/viewform/:formID/:workshopID" component={ViewForm}/>


                </Switch>
            </Router>
        );

    }


}

export default App;

