import React from 'react';
import {Button, Card, Icon, Table, Text, Dropdown, StampCard, Form, Avatar} from 'tabler-react';

class Messages extends React.Component {
    render() {
        return (
            <Card>
                <Card.Header>
                    <Card.Title>Messages</Card.Title>
                    <Card.Options>
                        <Form>
                            <Form.InputGroup>
                                <Form.Input
                                    icon="search"
                                    placeholder="Search Messages..."
                                    position="append"
                                    className="form-control-sm"/>
                            </Form.InputGroup>
                        </Form>
                    </Card.Options>
                </Card.Header>

                <Table cards={true} striped={true} responsive={true} className="table-vcenter">
                    <Table.Body>
                        <Table.Row>
                            <Table.Col> <Avatar imageURL="./demo/faces/male/9.jpg"/> </Table.Col>
                            <Table.Col>Ronald Bradley</Table.Col>
                            <Table.Col>Can you send ...</Table.Col>
                            <Table.Col>May 6, 2020</Table.Col>
                            <Table.Col> <Icon link={true} name="trash"/> </Table.Col>
                        </Table.Row>

                        <Table.Row>
                            <Table.Col> <Avatar>RG</Avatar> </Table.Col>
                            <Table.Col>Russell Gibson</Table.Col>
                            <Table.Col>Hello. What's up? ...</Table.Col>
                            <Table.Col>April 22, 2020</Table.Col>
                            <Table.Col> <Icon link={true} name="trash"/> </Table.Col>
                        </Table.Row>

                        <Table.Row>
                            <Table.Col> <Avatar imageURL="./demo/faces/female/1.jpg"/> </Table.Col>
                            <Table.Col>Beverly Armstrong</Table.Col>
                            <Table.Col>Let Get Started...</Table.Col>
                            <Table.Col>April 15, 2020</Table.Col>
                            <Table.Col> <Icon link={true} name="trash"/> </Table.Col>
                        </Table.Row>

                        <Table.Row>
                            <Table.Col> <Avatar imageURL="./demo/faces/male/4.jpg"/> </Table.Col>
                            <Table.Col>Bobby Knight</Table.Col>
                            <Table.Col>See you tomorrow...</Table.Col>
                            <Table.Col>April 8, 2020</Table.Col>
                            <Table.Col> <Icon link={true} name="trash"/> </Table.Col>
                        </Table.Row>

                        <Table.Row>
                            <Table.Col> <Avatar imageURL="./demo/faces/female/11.jpg"/> </Table.Col>
                            <Table.Col>Sharon Wells</Table.Col>
                            <Table.Col>How are you?...</Table.Col>
                            <Table.Col>April 6, 2020</Table.Col>
                            <Table.Col> <Icon link={true} name="trash"/> </Table.Col>
                        </Table.Row>

                    </Table.Body>
                </Table>
            </Card>
        );
    }
}

export default Messages;
