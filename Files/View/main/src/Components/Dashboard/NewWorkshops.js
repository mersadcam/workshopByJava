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

        const {workshops} = this.props

        return (
            <Card>
                <Card.Header>
                    <Card.Title> New Workshops </Card.Title>
                </Card.Header>
                <Carousel>
                    {workshops.map((item) => (
                        <WorkshopCard
                            title={item.workshop.name}
                            imageURL={item.imageURL}
                            avatarURL={item.avatarURL}
                            teacherUsername={item.teacher.username}
                            teacher={item.teacher.fullName}
                            date={item.workshop.startTime}
                            place={item.workshop.place}
                            price={item.workshop.value}
                            buttonText="View"
                            id={item.workshop.id}
                            buttonColor="primary"/>
                    ))}
                </Carousel>
            </Card>
        )
    }
}

export default NewWorkshops;
