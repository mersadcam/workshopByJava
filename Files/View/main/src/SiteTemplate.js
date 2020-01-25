import * as React from "react";

import {Site, Nav, AccountDropdown} from "tabler-react";

import "tabler-react/dist/Tabler.css";
import Search from "./Components/Workshops/Search";
import profile from "./Components/Profile/Profile.json";
import axios from "axios"

class SiteTemplate extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            userType: "",
            fullName: ""

        }
    }

    componentWillMount(): void {
        axios.get("http://localhost:8000/user/info").then(res => {
            this.setState({userType: res.data.body.user.userType, fullName: res.data.body.contactPoint.fullName})

        }).catch(e => {
            console.log(e)
        })
    }

    render() {
        return (
            <Site>
                <Site.Header imageURL={'/logo.png'}
                             navItems={
                                 <AccountDropdown
                                     avatarURL={profile.avatarURL}
                                     name={this.state.fullName}
                                     description={this.state.userType !== "user" ? this.state.userType.toUpperCase() : "Welcome Back"}
                                     options={[
                                         {icon: "user", value: "Profile", to: "/profile"},
                                         {icon: "settings", value: "Settings", to: "/settings"},
                                         {icon: "send", value: "Messages", badge: "6", to: "/messages"},
                                         {isDivider: true},
                                         {icon: "help-circle", value: "Need help?", to: "/help"},
                                         {icon: "log-out", value: "Sign out", to: "/signout"}]}
                                 />
                             }
                />
                <Site.Nav>
                    <Nav>
                        <Nav.Item value="Dashboard" icon="home" to={'/dashboard'}/>
                        <Nav.Item value="Workshops" icon="box" to={'/workshops'}/>
                        <Nav.Item value="Create Workshops" icon="plus" to={'/createworkshop'}/>
                        <Nav.Item value="Help" icon="" to={'/help'}/>
                    </Nav>
                </Site.Nav>
                {this.props.children}


                <Site.Footer
                    copyright={
                        <div>
                            Copyright © 2020 <a href="/"> Learnishop </a> All rights reserved.
                        </div>
                    }
                    links={[
                        <a href="#">First Link</a>,
                        <a href="#">Second Link</a>,
                        <a href="#">Third Link</a>,
                        <a href="#">Fourth Link</a>,
                        <a href="#">Five Link</a>,
                        <a href="#">Sixth Link</a>,
                        <a href="#">Seventh Link</a>,
                        <a href="#">Eigth Link</a>,
                    ]}
                    note="Lorem Ipsum is simply dummy text of the printing and typesetting industry.">
                </Site.Footer>
            </Site>
        )
    }
}

export default SiteTemplate;
