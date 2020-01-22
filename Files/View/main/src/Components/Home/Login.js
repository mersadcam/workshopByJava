import React from 'react';
import './Login.css';
import {Button, Form, Grid,Text} from 'tabler-react'
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
                        <Form onSubmit={(event) => console.log(event.target.name + 'clicked')} className="form-login px-5 pb-5">
                            <Grid.Row>
                                <Grid.Col>
                                    <Form.Group isRequired label="Username"> <Form.Input name="username"/> </Form.Group>
                                </Grid.Col>
                            </Grid.Row>
                            <Grid.Row>
                                <Grid.Col>
                                    <Form.Group isRequired label="Password"> <Form.Input name="password" type="password"/> </Form.Group>
                                </Grid.Col>
                            </Grid.Row>
                            <Grid.Row alignItem={'center'} className={'justify-content-center'}>
                                <Grid.Col>
                                    <Form.Checkbox isInline label="Keep Me Signed In" name="keepMeSignedIn"/>
                                </Grid.Col>
                                <Grid.Col>
                                    <Text.Small>
                                        <a href={'#'} id={"forgetPasswordButton"} onClick={this.forgetPasswordSwitch}> Forgot your password? </a>
                                    </Text.Small>
                                </Grid.Col>
                            </Grid.Row>
                            <Grid.Row className={'text-center'}>
                                <Grid.Col>
                                    <Button type='submit' color='primary' value='Login'>Login</Button>
                                </Grid.Col>
                            </Grid.Row>
                        </Form>}

                        {this.state.forgetPasswordShow &&
                        <Form onSubmit={(event) => console.log(event.target.name + 'clicked')} className="form-forgetPassword px-5 pb-5">
                            <Grid.Row>
                                <Grid.Col>
                                    <Form.Group isRequired label="Email or Username"> <Form.Input name="emailOrUsername"/> </Form.Group>
                                </Grid.Col>
                            </Grid.Row>
                            <Grid.Row className={'text-center'}>
                                <Grid.Col>
                                    <Button type='submit' color='primary' value='Send'>Send Reset Link</Button>
                                </Grid.Col>
                                <Grid.Col>
                                    <Button color='secondary' value='Go Back' onClick={this.forgetPasswordSwitch}>Go Back</Button>
                                </Grid.Col>
                            </Grid.Row>
                        </Form>}

                        {this.state.signupShow &&
                        <Form onSubmit={(event) => console.log(event.target.name + 'clicked')} className="form-signup px-5 pb-5">
                            <Grid.Row>
                                <Grid.Col>
                                    <Form.Group isRequired label="First Name"> <Form.Input name="firstname"/> </Form.Group>
                                </Grid.Col>
                                <Grid.Col>
                                    <Form.Group isRequired label="Last Name"> <Form.Input name="lastname"/> </Form.Group>
                                </Grid.Col>
                            </Grid.Row>
                            <Grid.Row>
                                <Grid.Col>
                                    <Form.Group isRequired label="Username"> <Form.Input name="username"/> </Form.Group>
                                </Grid.Col>
                                <Grid.Col>
                                    <Form.Group isRequired label="Password"> <Form.Input name="password" type="password"/> </Form.Group>
                                </Grid.Col>
                            </Grid.Row>
                            <Grid.Row>
                            <Grid.Col>
                                <Form.Group isRequired label="Email"> <Form.Input name="email"/> </Form.Group>
                            </Grid.Col>
                        </Grid.Row>
                            <Grid.Row alignItems={'center'} >
                                <Grid.Col>
                                    <Form.Group label="Gender">
                                        <Form.Select> <option> Male </option> <option> Female </option> <option> Other </option> </Form.Select>
                                    </Form.Group>
                                </Grid.Col>
                                <Grid.Col className={'text-center'}>
                                    <Button type='submit' color='primary' value='Signup'>Signup</Button>
                                </Grid.Col>
                            </Grid.Row>
                        </Form>}
                    </div>
                </div>
            </Animated>}
            </div>
        )
    }
}


export default Login;
