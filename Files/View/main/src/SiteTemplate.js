import * as React from "react";

import type, {
    Site,
    Nav,
    Grid,
    List,
    Button,
    RouterContextProvider,
} from "tabler-react";

import "tabler-react/dist/Tabler.css";
import {NavLink, withRouter} from "react-router-dom";
import Search from "./Components/Workshops/Search";
import profile from "./Components/Profile/Profile.json";
import axios from "axios"

class SiteTemplate extends React.Component {

    constructor(props) {
        super(props);
        this.state={
            userType:"",
            fullName:""

        }
    }

    componentWillMount(): void {
        axios.get("http://localhost:8000/user/info").then(res=>{
            this.setState({userType:res.data.body.user.userType,fullName:res.data.body.contactPoint.fullName})

        }).catch(e=>{
            console.log(e)
        })
    }

    accountDropdownProps = {
        avatarURL: profile.avatarURL,
        name: state.fullName,
        description: state.userType !== "user" ? state.userType.toUpperCase() : "Welcome Back",
        options: [
            {icon: "user", value: "Profile", to: "/profile"},
            {icon: "settings", value: "Settings",to: "/settings"},
            {icon: "send", value: "Messages", badge: "6", to: "/messages"},
            {isDivider: true},
            {icon: "help-circle", value: "Need help?",to: "/help"},
            {icon: "log-out", value: "Sign out", to: "/signout"},
        ],
    };

    headerProps = {
        href: "/",
        alt: "XSITE",
        imageURL: "/logo.png",
        accountDropdown: this.accountDropdownProps,
        navItems: (
            <Nav.Item type="div" className="d-none d-md-flex">
                <Search/>
            </Nav.Item>
        ),
    };

    navBarItems = [
        {
            value: "Dashboard",
            to: "/dashboard",
            icon: "home",
            LinkComponent: withRouter(NavLink),
            useExact: true,
        },
        {
            value: "Workshops",
            to: "/workshops",
            icon: "box",
        },
        {
            value: "Blog",
            to: "/page3",
            icon: "file",
        },
        {
            value: "About Us",
            to: "/page4",
            icon: "star",
        },
    ];

    footerProps = {
        links: [
            <a href="#">First Link</a>,
            <a href="#">Second Link</a>,
            <a href="#">Third Link</a>,
            <a href="#">Fourth Link</a>,
            <a href="#">Five Link</a>,
            <a href="#">Sixth Link</a>,
            <a href="#">Seventh Link</a>,
            <a href="#">Eigth Link</a>,
        ],
        note:
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
        copyright: (
            <div>
                Copyright © 2020
                <a href="Components/Dashboard"> XSITE </a>.
                All rights reserved.
            </div>
        ),
    }

    render() {
        return (
            <Site.Wrapper
                navProps={{itemsObjects: this.navBarItems}}
                headerProps={this.headerProps}
                routerContextComponentType={withRouter(RouterContextProvider)}
                footerProps={this.footerProps}
            >
                {this.props.children}
            </Site.Wrapper>
        )
    }
}

export default SiteTemplate;
