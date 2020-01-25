import React from 'react';
import Navbar from "./Navbar";
import Slideshow from "./Slideshow";
import Login from "./Login";
import {Button, Form, Grid} from "tabler-react";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'
import Search from "../Workshops/Search";

class Home extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            activeSlide: 1,
            slideCount: 2,
            loginShow: false,
            // outline: true,
        }
    }

    goToSlide = (dest) => {
        this.setState({activeSlide: dest})
    };

    toggleLogin = () => {
        this.setState({loginShow: !this.state.loginShow})
    };

    slideshowArray = [
        //Slideshow 1
        <Slideshow image="/image1-mask.png">
            {/*<img className={'logo-outline'} src={this.state.outline ? '/logo-outline.svg' : '/logo-color.svg'} alt={' '}/>*/}
            <h2> Learn More, Earn More </h2>
            <p>
                Explore Workshops and Pick One To Learn. <br/>
                Choose between Best Teachers and Best Lessons. </p>
            <p className={'mt-8'}>
                <Button className={'px-7 text-large'} color={'danger'}> Get Started </Button>
            </p>
        </Slideshow>,

        //Slideshow 2
        <Slideshow image="/image2-mask.png">
            <h2> Missingid Oido Otsui </h2>
            <p>
                Explore Workshops and Pick One To Learn. <br/>
                Choose between Best Teachers and Best Lessons. </p>
            <p className={'mt-8'}>
                <Button pill className={"btn-hover"} color="primary" outline onClick={() => this.goToSlide(1)}> Next </Button>
            </p>
        </Slideshow>,

    ]

    render() {
        return (
            <div>
                <Navbar toggleLogin={this.toggleLogin}/>
                <Login history={this.props.history} show={this.state.loginShow} toggleLogin={this.toggleLogin}/>
                <div className={"slideshow-container"}>
                    {this.slideshowArray[this.state.activeSlide - 1]}
                </div>
            </div>
        );
    }
}

export default Home;
