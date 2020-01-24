import React, {useCallback} from 'react';
import {Card, Grid, Page, Button, Header, Text, Tag, Avatar, Form} from "tabler-react";
import SiteTemplate from "../../SiteTemplate";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'
import profile from "./Profile.json";
import './Profile.css'
// import ReactCrop from 'react-image-crop';
// import 'react-image-crop/dist/ReactCrop.css'

class EditProfile extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            selectedProfile: null,
            selectedCover: null ,
            profileImageURL: profile.avatarURL ,
            coverImageURL: profile.coverURL ,
            // crop: { aspect: 1 / 1 }
        }
    }


    fileChangedHandler = event => {
        this.setState({
            selectedProfile: event.target.files[0]
        });

        let reader = new FileReader();

        reader.onloadend = () => {
            this.setState({
                profileImageURL: reader.result
            });
        };
        reader.readAsDataURL(event.target.files[0])

    };

    submit = () => {
        let fd = new FormData();
        fd.append('file', this.state.selectedProfile);
        let request = new XMLHttpRequest();
        request.onreadystatechange = function () {
            if (this.readyState === 4 && this.status === 200) {
                alert('Uploaded!');
            }
        };
        request.open("POST", "https://us-central1-tutorial-e6ea7.cloudfunctions.net/fileUpload", true);
        request.send(fd);
    };

    // changeCrop = crop => {
    //     this.setState({ crop });
    // };

    render() {
        return (
            <SiteTemplate>
                <Page.Content>

                    <Form onSubmit={(event) => console.log(event.target.name + 'clicked')}>
                        <img alt={profile.username + " Cover"} src={profile.coverURL}/>

                        <Card>
                            <Card.Body>
                                <Grid.Row alignItems={'center'}>
                                    <Grid.Col lg={3} className={'text-center'}>
                                        <img
                                            alt={profile.username + " Cover"}
                                            src={this.state.profileImageURL}
                                            className={'rounded-circle avatar-big'}/>
                                        <Form.Group className={'image-upload-wrapper mt-5'}>
                                            <Form.Input type={'file'} className={'image-upload-input'} name="avatar"
                                                        accept='image/*' onChange={this.fileChangedHandler}/>
                                            <Button className={'image-upload-button'} outline
                                                    size={'sm'} color={'secondary'}>Change</Button>
                                        </Form.Group>
                                    {/*    <ReactCrop src={this.state.imagePreviewUrl}*/}
                                    {/*    crop={this.state.crop}  circularCrop    onChange={newCrop => this.changeCrop(newCrop)}/>*/}
                                    </Grid.Col>


                                    <Grid.Col lg={9}>
                                        <Grid.Row>
                                            <Grid.Col lg={6}>
                                                <Form.Group isRequired label="Full Name"> <Form.Input
                                                    value={profile.fullName} name="fullName"/> </Form.Group>
                                            </Grid.Col>
                                            <Grid.Col lg={6}>
                                                <Form.Group isRequired label="Username"> <Form.Input
                                                    value={profile.username} name="username"/> </Form.Group>
                                            </Grid.Col>
                                        </Grid.Row>


                                        <Grid.Row>
                                            <Grid.Col lg={6}>
                                                <Form.Group isRequired label="Email"> <Form.Input
                                                    value={profile.email} name="email"/> </Form.Group>
                                            </Grid.Col>
                                            <Grid.Col lg={6}>
                                                <Form.Group label="Subtitle"> <Form.Input
                                                    value={profile.subtitle} name="subtitle"/> </Form.Group>
                                            </Grid.Col>
                                        </Grid.Row>


                                        <Grid.Row>
                                            <Grid.Col>
                                                <Form.Group label={'Bio'}> <Form.Textarea
                                                    defaultValue={profile.bio} name="bio"/></Form.Group>
                                            </Grid.Col>
                                        </Grid.Row>

                                        <Grid.Row className={'justify-content-center'}>
                                            <Grid.Col lg={4}>
                                                <Button type='submit' color='blue'
                                                        onClick={() => alert(this.state.profileImageURL)}>Apply</Button>
                                                <Button color='secondary' className={'ml-2'}>Cancel</Button>
                                            </Grid.Col>
                                        </Grid.Row>

                                    </Grid.Col>
                                </Grid.Row>
                            </Card.Body>
                        </Card>
                    </Form>
                </Page.Content>
            </SiteTemplate>
        )
    }
}

export default EditProfile;
