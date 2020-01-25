import React from 'react';
import './Login.css';
import {Button, Form, Grid,Text} from 'tabler-react'
import {Animated} from "react-animated-css";
import axios from "axios";
import {withRouter} from 'react-router'

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
            user:{},
            msg:""
        };
    } ;

    static getDerivedStateFromProps(props, state) {
        return {
            show: props.show
        };
    }

    setPassword(value) {

        const {user} = this.state;
        this.setState({user:{...user,password: value}})

    }

    setUsername(value) {

        const {user} = this.state;
        this.setState({user:{...user ,username: value}})
    }



    async sendUser(api){


        await axios.post("http://localhost:8000"+api, this.state.user )
            .then((res)=>{

                if( res.data.status == "true"){
                    localStorage.setItem("token",res.data.body.token);
                    localStorage.setItem("userType",res.data.body.userType);

                    this.props.history.push("/dashboard");

                }else{

                    this.setState({msg:res.data.msg})

                }


            })
            .catch((e)=>console.log(e))

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

        const {user} = this.state;

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
                        <div  className="form-login px-5 pb-5">
                            <Grid.Row>
                                <Grid.Col>
                                    <Form.Group isRequired label="Username" >

                                        <Form.Input name="username" onChange={(e)=>{

                                            this.setUsername(e.target.value)
                                        }}/>

                                    </Form.Group>
                                </Grid.Col>
                            </Grid.Row>
                            <Grid.Row>
                                <Grid.Col>
                                    <Form.Group isRequired label="Password"> <Form.Input name="password" type="password" onChange={(e)=>{
                                        this.setPassword(e.target.value)
                                    }}/> </Form.Group>
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

                            <p style={{color:"red"}}>{this.state.msg}</p>

                            <Grid.Row className={'text-center'}>
                                <Grid.Col>

                                    <Button  color='primary' value='Login' onClick={e => this.sendUser("/login") }>Login</Button>
                                </Grid.Col>
                            </Grid.Row>
                        </div>}


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
                        <div  className="form-signup px-5 pb-5">
                            <Grid.Row>
                                <Grid.Col>
                                    <Form.Group isRequired label="Full Name"> <Form.Input name="fullName"
                                    onChange={e => {
                                        this.setState({user:{...user,fullName:e.target.value}})
                                    }}
                                    /> </Form.Group>
                                </Grid.Col>
                            </Grid.Row>
                            <Grid.Row>
                                <Grid.Col>
                                    <Form.Group isRequired label="Username"> <Form.Input name="username"
                                    onChange={e=>{
                                        this.setState({user:{...user,username:e.target.value}})
                                    }}
                                    /> </Form.Group>
                                </Grid.Col>
                            </Grid.Row>

                            <Grid.Row>
                                <Grid.Col>
                                    <Form.Group isRequired label="Password"> <Form.Input name="password" type="password"
                                     onChange={e => {
                                         this.setState({user:{...user,password:e.target.value}})
                                     }}
                                    /> </Form.Group>
                                </Grid.Col>
                            </Grid.Row>
                            <Grid.Row>
                            <Grid.Col>
                                <Form.Group isRequired label="Email"> <Form.Input name="email"
                                  onChange={e => {
                                      this.setState({user:{...user,emailAddress:e.target.value}})
                                  }}
                                /> </Form.Group>
                            </Grid.Col>
                        </Grid.Row>

                            <p style={{color:"red"}}>{this.state.msg}</p>

                            <Grid.Row alignItems={'center'} >
                                <Grid.Col className={'text-center'}>
                                    <Button onClick={e=>this.sendUser("/register")} type='submit' color='primary' value='Signup' >Signup</Button>
                                </Grid.Col>
                            </Grid.Row>
                        </div>}


                    </div>
                </div>
            </Animated>}
            </div>
        )
    }
}


export default Login;
