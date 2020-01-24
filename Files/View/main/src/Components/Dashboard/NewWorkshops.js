import React from 'react';
import {Card} from 'tabler-react'
import Carousel from "../Carousel/Carousel";
import WorkshopCard from "../WokshopCard/WorkshopCard";
import newWorkshops from "./NewWorkshops.json";

class NewWorkshops extends React.Component {
    
    render() {
        return (
            <Card>
                <Card.Header>
                    <Card.Title> New Workshops </Card.Title>
                </Card.Header>
                <Carousel>
                    {newWorkshops.items.map((item, key) => (
                        <WorkshopCard
                            title={item.title}
                            imageURL={item.imageURL}
                            avatarURL={item.avatarURL}
                            teacher={item.teacher}
                            date={item.date}
                            place={item.place}
                            price={item.price}
                            buttonText="View"
                            id={item.id}
                            buttonColor="primary"/>
                    ))}
                </Carousel>
            </Card>
        )
    }
}

export default NewWorkshops;
