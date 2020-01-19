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
import Search from "./Components/WorkshopsPage/Search";


class SiteTemplate extends React.Component {

    accountDropdownProps = {
        avatarURL: "demo/faces/male/7.jpg",
        name: "Jane Pearson",
        options: [
            {icon: "user", value: "Profile"},
            {icon: "settings", value: "Settings"},
            {icon: "send", value: "Messages", badge: "6"},
            {isDivider: true},
            {icon: "help-circle", value: "Need help?"},
            {icon: "log-out", value: "Sign out"},
        ],
    };

    headerProps = {
        href: "/",
        alt: "XSITE",
        imageURL: "./logo.png",
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
