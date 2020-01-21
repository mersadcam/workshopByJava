import React from 'react';
import './Slideshow.css';
import {Button} from 'tabler-react';
import {Animated} from "react-animated-css";

class Slideshow extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            image: null};
    }

    static getDerivedStateFromProps(props, state) {
        return {
            image: props.image
        };
    }

    toggle = () => {
        this.setState({show: !this.state.show});
        this.props.changeActiveSlide(this.state.number);
    };

    render() {
        return (
                <div className="slideshow row">
                    <div className="slide-txt col-7 my-auto">
                        <div className="row justify-content-end">
                            <div className="col-10">
                                {this.props.children}
                            </div>
                        </div>
                    </div>
                    <div className="slide-img col-5"
                       style={{backgroundImage: 'url("' + this.state.image + '")'}}/>
                </div>
        )
    }
}

export default Slideshow;