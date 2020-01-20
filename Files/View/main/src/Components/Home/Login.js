import React from 'react';
import './Login.css';
import {Button} from 'reactstrap';
import {Animated} from "react-animated-css";

class Login extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            show: false,
            loginShow: true,
            signupShow: false,
            forgetPasswordShow: false,
            loginSwitchClass: "switch-active",
            signupSwitchClass: "switch-inactive",
        };
    } ;

    static getDerivedStateFromProps(props, state) {
        return {
            show: props.show
        };
    }


    switch = () => {
        if (this.state.forgetPasswordShow) {
            this.setState({forgetPasswordShow: false});
            this.setState({loginShow: false});
        } else {
            this.setState({loginShow: !this.state.loginShow});
        }
        this.setState({signupShow: !this.state.signupShow});
        this.setState({loginSwitchClass: this.state.loginSwitchClass === "switch-active" ? "switch-inactive" : "switch-active"});
        this.setState({signupSwitchClass: this.state.signupSwitchClass === "switch-active" ? "switch-inactive" : "switch-active"});
    };

    forgetPasswordSwitch = () => {
        this.setState({loginShow: !this.state.loginShow});
        this.setState({forgetPasswordShow: !this.state.forgetPasswordShow});
    };


    render() {
        return (
            <div> {this.state.show && <Animated animationIn="fadeIn"
                                                animationOut="fadeOut"
                                                isVisible={this.state.show}
                                                animationInDuration={"300"}
                                                animationOutDuration={"300"}
                                                className={"login-container"}>
                <div className="d-flex justify-content-center">
                    <div className="login-frame rounded col-xl-3 col-lg-5 col-md-6 col-sm-8 my-9 py-4">
                        <a className="btn-close text-primary" onClick={this.props.toggleLogin}> &times; </a>
                        <div className="login-nav text-primary row justify-content-center mb-6">
                            <a className={"col-4 text-center switch " + this.state.loginSwitchClass}
                               onClick={this.switch}>Login</a>
                            <a className={"col-4 text-center switch " + this.state.signupSwitchClass}
                               onClick={this.switch}>Signup</a>
                        </div>

                        {this.state.loginShow &&
                        <form className="form-login px-4 pb-5" action="" method="post" name="form-login">
                            <label htmlFor="username">Username</label>
                            <input className="form-styling" type="text" name="username" placeholder=""/>

                            <label htmlFor="password">Password</label>
                            <input className="form-styling" type="password" name="password" placeholder=""/>
                            <div className="text-center">
                                <div className={"keepMeSignedIn"}>
                                    <input type="checkbox" id="keepMeSignedInCheckbox"/>
                                    <label htmlFor="keepMeSignedInCheckbox" id={"keepMeSignedInLabel"}>
                                        Keep me signed in</label>
                                </div>
                                <Button color={"primary"} className="rounded-pill" href={"#"}> Login </Button>
                                <a className="rounded-pill mt-5 ml-5" id={"forgetPasswordButton"}
                                        onClick={this.forgetPasswordSwitch}> Forgot your password? </a>
                            </div>
                        </form>}

                        {this.state.forgetPasswordShow &&
                        <form className="form-forgetPassword px-5 pb-5" action="" method="post"
                              name="form-forgetPassword">
                            <label htmlFor="email">Email Or Username</label>
                            <input className="form-styling" type="text" name="email" placeholder=""/>
                            <div className="text-center">
                                <Button color={"primary"} className="rounded-pill" href={"#"}> Send </Button>
                                <Button color="" className="rounded-pill"
                                        onClick={this.forgetPasswordSwitch}> Go Back </Button>
                            </div>
                        </form>}

                        {this.state.signupShow &&
                        <form className="form-signup px-5 pb-5" action="" method="post" name="form-signup">
                            <label htmlFor="firstName">Full name</label>
                            <input className="form-styling" type="text" name="firstName" placeholder=""/>

                            <label htmlFor="lastName">Full name</label>
                            <input className="form-styling" type="text" name="lastName" placeholder=""/>

                            <label htmlFor="email">Email</label>
                            <input className="form-styling" type="text" name="email" placeholder=""/>

                            <label htmlFor="password">Password</label>
                            <input className="form-styling" type="password" name="password" placeholder=""/>

                            <div className="text-center">
                                <a className="btn btn-primary text-white rounded-pill ">Sign Up</a>
                            </div>
                        </form>}
                    </div>
                </div>
            </Animated>}
            </div>
        )
    }
}


export default Login;
