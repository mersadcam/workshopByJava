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
            fullName: "",
            username:""

        }
    }

    componentWillMount(): void {
        axios.get("http://localhost:8000/user/info").then(res => {
            this.setState({userType: res.data.body.user.userType, fullName: res.data.body.contactPoint.fullName
            ,username:res.data.body.user.username})

        }).catch(e => {
            console.log(e)
        })
    }

    render() {
        return (
            <Site>
                <Site.Header imageURL={'/logo.png'}
                             href={'/'}
                             navItems={
                                 <AccountDropdown
                                     avatarURL={'/default-avatar.png'}
                                     name={this.state.fullName}
                                     description={this.state.userType !== "user" ? this.state.userType.toUpperCase() : ""}
                                     options={[
                                         {icon: "user", value: "Profile", to: "/profile/"+this.state.username},
                                         {icon: "settings", value: "Settings", to: "/settings"},
                                         {icon: "send", value: "Messages", badge: "6", to: "/messages"},
                                         {isDivider: true},
                                         {icon: "help-circle", value: "Need help?", to: "/help"},
                                         {icon: "log-out", value: "Sign out", to: "/"}]}
                                 />
                             }
                />
                <Site.Nav>
                    <Nav>
                        <Nav.Item value="Dashboard" icon="home" to={'/dashboard'}/>
                        <Nav.Item value="Workshops" icon="layers" to={'/workshops'}/>
                        {this.state.userType!="user"?
                            <Nav.Item value="Create Workshops" icon="plus" to={'/createworkshop'}/>
                            :
                            <div></div>
                        }
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
                        <a href="/workshops">Wokshops</a>,
                        <a href="/help">Help</a>,
                        <a href="/aboutus">About Us</a>,
                        <a href="/contactus">Contact Us</a>,
                        <a href="/sitemap">Site Map</a>,
                        <a href="/social">Social Media</a>,
                    ]}
                    note="">
                </Site.Footer>
            </Site>
        )
    }
}

export default SiteTemplate;
