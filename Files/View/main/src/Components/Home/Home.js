import React from 'react';
import Navbar from "./Navbar";
import Login from "./Login";
import {Button, Form, Grid, List} from "tabler-react";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {Animated} from 'react-animated-css';
import './Slideshow.css'

class Home extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            loginShow: false,
        }
    }

    toggleLogin = () => {
        this.setState({loginShow: !this.state.loginShow})
    };

    render() {
        return (
            <div>
                <Navbar toggleLogin={this.toggleLogin}/>
                <Login history={this.props.history} show={this.state.loginShow} toggleLogin={this.toggleLogin}/>
                <Animated className={"slideshow-container"}>
                    <div className="slideshow row">
                        <div className="slide-txt col-6">
                            <div className="row justify-content-end">
                                <div className="col-10 text-large">
                                    <h1 className={'text-weight-light'}> Learn More, Earn More </h1>
                                    <p className={'text-large'}>
                                        Explore Workshops and Pick One To Learn. <br/>
                                        Choose between Best Teachers and Best Lessons. </p>
                                    <p className={'mt-8'}>
                                        <Button className={'px-7 text-large'} color={'danger'} onClick={this.toggleLogin}> Get Started </Button>
                                    </p>
                                </div>
                                <div className="slide-img mt-8">
                                    <img src={'/image2-mask.png'} alt={' '}/>
                                </div>
                            </div>
                        </div>
                        <div className="slide-img col-6">
                            <img src={'/image1-mask.png'} alt={' '}/>
                            <div className="slide-txt">
                                <div className="row justify-content-end">
                                    <div className="col-10 text-large">
                                        <h1 className={'text-weight-light'}> Wide Subjects To Learn </h1>
                                        <p className={'text-large'}>
                                            Just Signup Now and Enjoy from Learning!
                                        </p>
                                        <p className={'text-large'}>
                                            <List unstyled>
                                                <List.Item>
                                                    <Button icon={'code'} color={'red'}>Programming</Button>
                                                </List.Item>
                                                <List.Item>
                                                    <Button color={'yellow'}>
                                                        <FontAwesomeIcon icon={'pen-nib'}/>Graphic Design</Button>
                                                </List.Item>
                                                <List.Item>
                                                    <Button color={'green'}>
                                                        <FontAwesomeIcon icon={'atom'}/>Science</Button>
                                                </List.Item>
                                                <List.Item>
                                                    <Button icon={'plus'} color={'primary'}>More</Button>
                                                </List.Item>
                                            </List>
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                </Animated>
            </div>
        );
    }
}

export default Home;
