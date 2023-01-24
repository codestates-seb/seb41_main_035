import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
const BREAK_POINT_TABLET = 768;

const Items = ['아우터', '상의', '하의', '원피스', '모자', '신발'];
const links = ['outer', 'top', 'bottom', 'onepiece', 'hat', 'shoes'];

const Sidebar = () => {
  const usenavigate = useNavigate();
  return (
    <SWrapper>
      <SidebarBox>
        <div>
          <p className="title">카테고리</p>
        </div>
        <ItemList>
          <div>
            {Items.map((item, index) => (
              <SList
                key={index}
                className="items"
                onClick={() => usenavigate(`/category/${links[index]}`)}
              >
                {item}
              </SList>
            ))}
          </div>
        </ItemList>
      </SidebarBox>
      <SLeftBoard />
    </SWrapper>
  );
};

const SList = styled.div``;
//전체부분
const SWrapper = styled.div`
  position: fixed;
  display: flex;
  width: 18%;
  justify-content: flex-end;
  min-width: 110px;
`;

const SidebarBox = styled.div`
  /* position: fixed; */
  .title {
    font-size: 20px;
    font-weight: 700;
    color: #4e4e4e;
    padding: 40px 30px 20px 0px;
    font-family: 'Gowun Batang', serif;
  }
`;
const ItemList = styled.div`
  /* border-right: 2px solid black; */
  .items {
    font-size: 15px;
    cursor: pointer;
    padding: 10px 3px;
    :hover {
      color: #196ba5;
      background-color: #f4f1e0;
    }
  }
`;
const SLeftBoard = styled.div`
  position: fixed;
  border-left: 2px solid gray;
  margin: 50px 0px 0px 20px;
  height: 400px;
`;
export default Sidebar;
