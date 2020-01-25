import * as React from "react";

import {Site, Nav, AccountDropdown} from "tabler-react";

import "tabler-react/dist/Tabler.css";
import Search from "./Components/Workshops/Search";
import profile from "./Components/Profile/Profile.json";

class SiteTemplate extends React.Component {

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
            <Site
                // headerProps={this.headerProps}
                // footerProps={this.footerProps}
            >
                <Site.Header imageURL={'/logo.png'}
                             navItems={
                                     <AccountDropdown
                                         avatarURL="./demo/faces/female/25.jpg"
                                         name="Jane Pearson"
                                         description="Administrator"
                                         options={[
                                             {icon: "settings", value: "Settings", to: "/settings"},
                                             "mail",
                                             "message",
                                             "divider",
                                             "help",
                                             "logout",
                                         ]}
                                     />
                             }
                             />

                <Site.Nav>
                    <Nav>
                        <Nav.Item value="Dashboard" icon="home" to={'/dashboard'}/>
                        <Nav.Item value="Workshops" icon="box" to={'/workshops'}/>
                        <Nav.Item value="Create Workshops" icon="plus" to={'/workshops'}/>
                        <Nav.Item value="Help" icon="" to={'/help'}/>
                    </Nav>
                </Site.Nav>
                {this.props.children}
            </Site>
        )
    }
}

export default SiteTemplate;
