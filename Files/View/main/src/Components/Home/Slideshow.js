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

    render() {
        return (
                <div className="slideshow row">
                    <div className="slide-txt col-6 my-auto">
                        <div className="row justify-content-end">
                            <div className="col-10">
                                {this.props.children}
                            </div>
                        </div>
                    </div>
                    <div className="slide-img col-6">
                         <img src={this.state.image} alt={' '}/>
                    </div>
                </div>
        )
    }
}

export default Slideshow;
