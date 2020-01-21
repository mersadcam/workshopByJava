import React from 'react';
import { Button } from 'tabler-react';
import './Navbar.css'

class Navbar extends React.Component {
    render() {
        return (
            <nav className="navbar navbar-expand container">
                <a className="navbar-brand nav-item mr-5" href="#">
                    <img className="navbar-logo" src="./logo.png" alt={"logo"}/></a>
                <ul className="navbar-nav">
                    <li className="nav-item"><Button pill color={"primary"} href={"#"}> Courses </Button> </li>
                    <li className="nav-item"><Button pill color={"primary"} href={"#"}> Teach at X </Button> </li>
                    <li className="nav-item"><Button pill color={"primary"} href={"#"}> About Us </Button> </li>
                </ul>
                <ul className="navbar-nav ml-auto">
                    <li className="nav-item"><Button pill color={"primary"} href={"#"} onClick={this.props.toggleLogin}> Login/Signup </Button> </li>
                </ul>
            </nav>
        );
    }
}

export default Navbar;