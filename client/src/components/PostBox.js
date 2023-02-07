/* eslint-disable react/prop-types */
import styled from 'styled-components';
import { BREAK_POINT_PC, BREAK_POINT_TABLET } from '../constants/index';
import Post from './Post';

// Home.js의 PostBox의 data = 상품배열
const PostBox = ({ data }) => {
  return (
    <SWrapper>
      <Container>
        {data.map((post) => (
          <Post key={post.boardId} post={post} />
        ))}
      </Container>
    </SWrapper>
  );
};

const SWrapper = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  /* z-index: 300; */
`;
const Container = styled.div`
  display: grid;
  grid-template-columns: 320px 320px 320px;
  @media only screen and (max-width: ${BREAK_POINT_PC}px) {
    & {
      grid-template-columns: 320px 320px;
    }
  }
  @media only screen and (max-width: ${BREAK_POINT_TABLET}px) {
    & {
      grid-template-columns: 70vw;
    }
  }
`;

export default PostBox;
