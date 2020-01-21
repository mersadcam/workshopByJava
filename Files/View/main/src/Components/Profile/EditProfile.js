import React from 'react';
import {Card, Grid, Page, Avatar, Header, Text, Tag, Form} from "tabler-react";
import 'react-dropzone-uploader/dist/styles.css'
import Dropzone from 'react-dropzone-uploader'
import SiteTemplate from "../../SiteTemplate";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'
import profile from "./Profile.json";


/**
 import 'react-dropzone-uploader/dist/styles.css'
 import Dropzone from 'react-dropzone-uploader'
 **/

const MyUploader = () => {
    // specify upload params and url for your files
    const getUploadParams = ({ meta }) => { return { url: 'localhost:8000' } } ;

    // called every time a file's `status` changes
    const handleChangeStatus = ({ meta, file }, status) => { console.log(status, meta, file) } ;

    // receives array of files that are done uploading when submit button is clicked
    const handleSubmit = (files, allFiles) => {
        console.log(files.map(f => f.meta)) ;
        allFiles.forEach(f => f.remove())
    } ;

    return (
        <Dropzone
            getUploadParams={getUploadParams}
            onChangeStatus={handleChangeStatus}
            onSubmit={handleSubmit}
            accept="image/*,audio/*,video/*"
        />
    )
} ;

class EditProfile extends React.Component {

    render() {
        return (
            <SiteTemplate>
                <Page.Content>
                    <MyUploader />
                    <Grid.Row className={'justify-content-center'}>
                        <Grid.Col xl={5} lg={5}>
                            <Card>
                                <Card.Body>
                                    <Grid.Row alignItems={'center'} className={'text-center'}>
                                        <Grid.Col lg={3}>
                                            <Avatar size="xxl" imageURL={profile.avatarURL}/>
                                        </Grid.Col>
                                        <Grid.Col lg={9}>
                                            <Grid.Row>
                                                <Header.H3 className={'text-weight-light mt-3'}>
                                                    {profile.firstName} {profile.lastName} {profile.username !== "user" &&
                                                    <Text transform={'uppercase d-inline ml-3'}> <Tag color={'blue'}> {profile.userType} </Tag> </Text>}
                                                </Header.H3>
                                            </Grid.Row>
                                            <Grid.Row className={'mt-3'}> <Tag className={'mr-2'}> Email </Tag> {profile.email} </Grid.Row>
                                            <Grid.Row className={'mt-3'}> <Tag className={'mr-2'}> Gender </Tag> {profile.gender} </Grid.Row>
                                        </Grid.Col>
                                    </Grid.Row>
                                    <Grid.Row className={'justify-content-center'}>
                                        <a href={'#'} className={'text-inherit mt-5'}><FontAwesomeIcon color={'gray'} icon={['far', 'edit']}/> Edit</a>
                                    </Grid.Row>
                                </Card.Body>
                            </Card>
                        </Grid.Col>
                    </Grid.Row>
                </Page.Content>
            </SiteTemplate>
        )
    }
}

export default EditProfile;
