import React from 'react';
import {Card} from 'tabler-react'
import Carousel from "../Carousel/Carousel";
import WorkshopCard from "../WokshopCard/WorkshopCard";
import newWorkshops from "./NewWorkshops.json";

class NewWorkshops extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {


        return (
            <Card>
                <Card.Header>
                    <Card.Title> New Workshops </Card.Title>
                </Card.Header>
                <Carousel>
                    {this.props.workshops.map((item) => (
                        <WorkshopCard
                            title={item.name}
                            imageURL={item.imageURL}
                            avatarURL={item.avatarURL}
                            teacher={item.teacher}
                            date={item.startTime}
                            place={item.place}
                            price={item.value}
                            buttonText="View"
                            buttonURL={'/workshop'}
                            buttonColor="primary"/>
                    ))}
                </Carousel>
            </Card>
        )
    }
}

export default NewWorkshops;
