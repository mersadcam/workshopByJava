import React, {useCallback} from 'react';
import {Card, Grid, Page, Button, Header, Text, Tag, Avatar, Form,Alert} from "tabler-react";
import SiteTemplate from "../../SiteTemplate";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'
import profile from "./Profile.json";
import './Profile.css'
import axios from "axios"
// import ReactCrop from 'react-image-crop';
// import 'react-image-crop/dist/ReactCrop.css'

class EditProfile extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            selectedProfile: null,
            selectedCover: null ,
            profileImageURL: '/default-avatar.png' ,
            coverImageURL: '/default-cover.jpg' ,
            user:{},
            contactPoint:{},
            msg:"",
            status:true,
            // crop: { aspect: 1 / 1 }
        }
    }

    async componentDidMount(): void {
        await axios.post("http://localhost:8000/user/info").then(res=>{
            this.setState({user:res.data.body.user,contactPoint:res.data.body.contactPoint})
        }).catch(e=>{
            console.log(e)
        })
    }

    onApply(){

        const toSend = {
            username:this.state.user.username,
            emailAddress:this.state.contactPoint.emailAddress,
            fullName:this.state.contactPoint.fullName,
            biography:this.state.contactPoint.biography,
            subTitle:this.state.contactPoint.subTitle
        };

        axios.post("http://localhost:8000/user/profile/edit",toSend).then(res=>{
            this.setState({status:res.data.status,msg:res.data.msg})

            if(this.state.status) {
                this.props.history.push("/profile/" + this.state.user.username);
            }


        }).catch(e=>{
            console.log(e)
        })

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

        const {user,contactPoint} = this.state

        return (
            <SiteTemplate>
                <Page.Content>

                    <div onSubmit={(event) => console.log(event.target.name + 'clicked')}>
                        <img alt={user.username + " Cover"} src={profile.coverURL}/>

                        <Card>
                            <Card.Body>
                                <Grid.Row alignItems={'center'}>
                                    <Grid.Col lg={3} className={'text-center'}>
                                        <img
                                            alt={user.username + " Cover"}
                                            src={this.state.profileImageURL}
                                            className={'rounded-circle avatar-big'}/>
                                        <Form.Group className={'image-upload-wrapper mt-5'}>
                                            <Form.Input type={'filae'} className={'image-upload-input'} name="avatar"
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
                                                    value={contactPoint.fullName} name="fullName"

                                                    onChange={e => {
                                                        this.setState({contactPoint:{...contactPoint,fullName:e.target.value}})
                                                    }}

                                                /> </Form.Group>
                                            </Grid.Col>
                                            <Grid.Col lg={6}>
                                                <Form.Group isRequired label="Username"> <Form.Input
                                                    value={user.username} name="username"

                                                    onChange={e => {
                                                        this.setState({user:{...user,username:e.target.value}})
                                                    }}

                                                /> </Form.Group>
                                            </Grid.Col>
                                        </Grid.Row>


                                        <Grid.Row>
                                            <Grid.Col lg={6}>
                                                <Form.Group isRequired label="Email"> <Form.Input
                                                    value={contactPoint.emailAddress} name="email"

                                                    onChange={e => {
                                                        this.setState({contactPoint:{...contactPoint,emailAddress:e.target.value}})
                                                    }}

                                                /> </Form.Group>
                                            </Grid.Col>
                                            <Grid.Col lg={6}>
                                                <Form.Group label="Subtitle"> <Form.Input
                                                    value={contactPoint.subTitle} name="subtitle"

                                                    onChange={e => {
                                                        this.setState({contactPoint:{...contactPoint,subTitle:e.target.value}})
                                                    }}

                                                /> </Form.Group>
                                            </Grid.Col>
                                        </Grid.Row>


                                        <Grid.Row>
                                            <Grid.Col>
                                                <Form.Group label={'Bio'}> <Form.Textarea
                                                    defaultValue={contactPoint.biography} name="bio"

                                                    onChange={e => {
                                                        this.setState({contactPoint:{...contactPoint,biography:e.target.value}})
                                                    }}
                                                /></Form.Group>
                                            </Grid.Col>
                                        </Grid.Row>
                                        <Grid.Row>
                                            <Grid.Col>

                                                {
                                                    !this.state.status ?
                                                        <Alert type={"danger"}>
                                                            {this.state.msg}
                                                        </Alert>
                                                        :
                                                        <div></div>

                                                }

                                            </Grid.Col>

                                        </Grid.Row>
                                        <Grid.Row className={'justify-content-center'}>
                                            <Grid.Col lg={4}>
                                                <Button type='submit' color='blue'
                                                        onClick={e=>{this.onApply()}}>Apply</Button>
                                                <Button color='secondary' className={'ml-2'}>Cancel</Button>
                                            </Grid.Col>
                                        </Grid.Row>

                                    </Grid.Col>
                                </Grid.Row>
                            </Card.Body>
                        </Card>
                    </div>
                </Page.Content>
            </SiteTemplate>
        )
    }
}

export default EditProfile;
