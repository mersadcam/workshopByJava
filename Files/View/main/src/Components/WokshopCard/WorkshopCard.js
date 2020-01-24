import React from 'react';
import {Card, Grid, Button, Avatar, Header, Text} from "tabler-react";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'

class WorkshopCard extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            id: null,
            title : null,
            imageURL: null,
            avatarURL: null,
            teacher: null,
            teacherUsername: null,
            date: null,
            place: null,
            price: null,
            buttonText: null,
            buttonColor: null,
        }
    }

    static getDerivedStateFromProps(props, state) {
        return {
            id: props.id,
            title : props.title,
            imageURL: props.imageURL,
            avatarURL: props.avatarURL,
            teacher: props.teacher,
            teacherUsername: props.teacherUsername,
            date: props.date,
            place: props.place,
            price: props.price,
            buttonText: props.buttonText,
            buttonColor: props.buttonColor,
        };
    }

    render() {
        return (
            <React.Fragment>
                <img
                    alt={this.state.title}
                    src={this.state.imageURL}
                />
                <Card>
                    <Card.Body>
                        <Grid.Row>
                            <Grid.Col width={10}>
                                <Header.H4><a href={'#'}>{this.state.title}</a></Header.H4>
                            </Grid.Col>
                            {/*<Grid.Col width={2}>*/}
                            {/*    <a href={'#'}><FontAwesomeIcon size='sm' color={'gray'} icon={['far', 'bookmark']}/></a>*/}
                            {/*</Grid.Col>*/}
                        </Grid.Row>
                        <Text color={'gray'}>
                            <FontAwesomeIcon size='sm' icon={"map-marker-alt"}/>
                            <Text.Small className={'mx-2'}> {this.state.place} </Text.Small>
                            <FontAwesomeIcon size='sm' icon={"calendar-check"}/>
                            <Text.Small className={'mx-2'}> {this.state.date} </Text.Small>
                            <FontAwesomeIcon size='sm' icon={"credit-card"}/>
                            <Text.Small className={'mx-2'}> {this.state.price} </Text.Small>
                        </Text>
                        <Grid.Row className={'mt-3'}>
                            <Grid.Col lg={1} md={1} sm={1}>
                                <Avatar size="sm" imageURL={this.state.avatarURL}/>
                            </Grid.Col>
                            <Grid.Col lg={6} md={3} sm={3}>
                                <a className={'text-inherit text-nowrap text-gray mx-2'} href={"/profile/" + this.state.teacherUsername}>{this.state.teacher}</a>
                            </Grid.Col>
                            <Grid.Col lg={2} md={10} sm={10}>
                                <Button color={this.state.buttonColor} size="sm" RootComponent={'a'} href={"/workshop/" + this.state.id}>{this.state.buttonText}</Button>
                            </Grid.Col>
                        </Grid.Row>
                    </Card.Body>
                </Card>
            </React.Fragment>
        )
    }
}

export default WorkshopCard;
