import React from 'react';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'
import ItemsCarousel from "react-items-carousel";

class Carousel extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            activeItemIndex: 0
        }
    }
    render() {
        return (
                <ItemsCarousel
                    infiniteLoop={true}
                    gutter={12}
                    activePosition={'center'}
                    chevronWidth={60}
                    disableSwipe={false}
                    alwaysShowChevrons={true}
                    numberOfCards={4}
                    slidesToScroll={1}
                    outsideChevron={true}
                    showSlither={true}
                    firstAndLastGutter={true}
                    activeItemIndex={this.state.activeItemIndex}
                    requestToChangeActive={value => this.setState({ activeItemIndex: value })}
                    rightChevron={<FontAwesomeIcon icon={'chevron-right'}/>}
                    leftChevron={<FontAwesomeIcon icon={'chevron-left'}/>}
                >
                    {this.props.children}
                </ItemsCarousel>
            )
    }
}

export default Carousel;
